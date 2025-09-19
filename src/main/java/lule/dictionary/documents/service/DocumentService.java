package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.*;
import lule.dictionary.documents.data.attribute.DocumentFormAttribute;
import lule.dictionary.documents.data.attribute.DocumentListAttribute;
import lule.dictionary.documents.data.documentSubmission.SubmissionStrategy;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.documents.data.exception.DocumentServiceException;
import lule.dictionary.documents.data.request.*;
import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.documentSubmission.ContentSubmissionStrategy;
import lule.dictionary.documents.data.documentSubmission.UrlSubmissionStrategy;
import lule.dictionary.familiarity.service.FamiliarityService;
import lule.dictionary.jsoup.service.exception.InvalidUriException;
import lule.dictionary.language.service.Language;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.documents.service.exception.DocumentNotFoundException;
import lule.dictionary.documents.data.selectable.Phrase;
import lule.dictionary.documents.data.selectable.Selectable;
import lule.dictionary.documents.data.selectable.Word;
import lule.dictionary.documents.data.repository.DocumentRepository;
import lule.dictionary.jsoup.service.JsoupService;
import lule.dictionary.pagination.service.PaginationService;
import lule.dictionary.pagination.data.DocumentPaginationData;
import lule.dictionary.translations.data.request.ExtractPhrasesRequest;
import lule.dictionary.translations.data.request.FindTranslationsInDocumentRequest;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.ValidationException;
import lule.dictionary.validation.service.Validator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final Validator validator;
    private final PaginationService paginationService;
    private final JsoupService jsoupService;
    private final TranslationService translationService;
    private final PatternService patternService;
    private final FamiliarityService familiarityService;
    private final DocumentSanitizer documentSanitizer;
    private final DocumentsLocalizationService documentsLocalization;

    @Transactional
    public Result<Integer> createDocument(CreateDocumentRequest request) {
        UserProfile principal = (UserProfile) request.authentication().getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(principal.userInterfaceLanguage());
        SubmissionStrategy submissionStrategy = switch (request.submissionStrategy()) {
            case "url_submit" -> UrlSubmissionStrategy.of(request.title(), request.url(), localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
            case "content_submit" -> ContentSubmissionStrategy.of(request.title(), request.content(), localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
            default -> throw new IllegalStateException("Unexpected value: " + request.submissionStrategy());
        };
        return switch (submissionStrategy) {
            case UrlSubmissionStrategy urlSubmission -> {
                Result<?> result = validator.validate(List.of(
                        Constraint.of("title", request.title()::isBlank, switch (uiLanguage) {
                            case PL -> "Tytuł nie może być pusty";
                            case EN -> "Title cannot be empty";
                            case IT -> "Il titolo non può essere vuoto";
                            case NO -> "Tittelen kan ikke være tom";
                        }),
                        Constraint.of("title", () -> request.title().length() > 500, switch (uiLanguage) {
                            case PL -> "Tytuł nie może być dłuższy niż 500 znaków";
                            case EN -> "Title cannot be longer than 500 characters";
                            case IT -> "Il titolo non può essere più lungo di 500 caratteri";
                            case NO -> "Tittelen kan ikke være lenger enn 500 tegn";
                        }),
                        Constraint.of("url", () -> request.url().isBlank(), switch (uiLanguage) {
                            case PL -> "URL nie może być pusty";
                            case EN -> "URL cannot be empty";
                            case IT -> "L'URL non può essere vuoto";
                            case NO -> "URL-en kan ikke være tom";
                        }),
                        Constraint.of("url", () -> request.url().length() > 500, switch (uiLanguage) {
                            case PL -> "URL nie może być dłuższy niż 500 znaków";
                            case EN -> "URL cannot be longer than 500 characters";
                            case IT -> "L'URL non può essere più lungo di 500 caratteri";
                            case NO -> "URL-en kan ikke være lenger enn 500 tegn";
                        })
                ));
                yield switch (result) {
                    case Ok<?> ignored -> {
                        String content = jsoupService.importDocumentContent(urlSubmission.url());
                        yield  Ok.of(insertIntoDatabase(InsertIntoDatabaseRequest.builder()
                                .title(urlSubmission.title())
                                .url(urlSubmission.url())
                                .content(content)
                                .userDetails(principal)
                                .build()));
                    }
                    case Err<?> v -> v.throwable() instanceof ValidationException validationException ?
                            Err.of(new DocumentServiceException(DocumentFormAttribute.of(submissionStrategy, localization), validationException.getViolations())) :
                            Err.of(new RuntimeException());
                };

            }
            case ContentSubmissionStrategy contentSubmission -> {
                Result<?> result = validator.validate(List.of(
                        Constraint.of("title", request.title()::isBlank, switch (uiLanguage) {
                            case PL -> "Tytuł nie może być pusty";
                            case EN -> "Title cannot be empty";
                            case IT -> "Il titolo non può essere vuoto";
                            case NO -> "Tittelen kan ikke være tom";
                        }),
                        Constraint.of("title", () -> request.title().length() > 500, switch (uiLanguage) {
                            case PL -> "Tytuł nie może być dłuższy niż 500 znaków";
                            case EN -> "Title cannot be longer than 500 characters";
                            case IT -> "Il titolo non può essere più lungo di 500 caratteri";
                            case NO -> "Tittelen kan ikke være lenger enn 500 tegn";
                        }),
                        Constraint.of("content", () -> request.content().isBlank(), switch (uiLanguage) {
                            case PL -> "Treść nie może być pusta";
                            case EN -> "Content cannot be empty";
                            case IT -> "Il contenuto non può essere vuoto";
                            case NO -> "Innholdet kan ikke være tomt";
                        }),
                        Constraint.of("content", () -> request.content().length() > 100000, switch (uiLanguage) {
                            case PL -> "Treść nie może być dłuższa niż 100 000 znaków";
                            case EN -> "Content cannot be longer than 100,000 characters";
                            case IT -> "Il contenuto non può essere più lungo di 100.000 caratteri";
                            case NO -> "Innholdet kan ikke være lenger enn 100 000 tegn";
                        })
                ));
                yield switch (result) {
                    case Ok<?> ignored -> {
                        String content = contentSubmission.content();
                        yield  Ok.of(insertIntoDatabase(InsertIntoDatabaseRequest.builder()
                                .title(contentSubmission.title())
                                .url("")
                                .content(content)
                                .userDetails(principal)
                                .build()));
                    }
                    case Err<?> v -> v.throwable() instanceof ValidationException validationException ?
                            Err.of(new DocumentServiceException(DocumentFormAttribute.of(submissionStrategy, localization), validationException.getViolations())) :
                            Err.of(new RuntimeException());
                };
            }
        };
    }

    public DocumentListAttribute findMany(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        List<DocumentWithTranslationData> documents = documentRepository.findByOwnerAndTargetLanguage(principal.getUsername(), principal.targetLanguage());
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(principal.userInterfaceLanguage());
        return DocumentListAttribute.of(documents, localization);
    }

    public Result<DocumentAttribute> loadDocumentContent(LoadDocumentContentRequest request) {
        return (Result<DocumentAttribute>) documentRepository.findById(request.documentId(), request.page())
                .map(found -> {
                    Result<?> result = documentSanitizer.validateNumberOfPages(SanitizeNumberOfPagesRequest.of(request.page(), paginationService.getNumberOfPages(found.totalContentLength())));
                    switch (result) {
                        case Ok<?> ignored -> {
                            AssembleDocumentContentData assembleContentRequest = AssembleDocumentContentData.builder()
                                    .selectableId(request.wordId())
                                    .documentId(request.documentId())
                                    .contentBlob(found.pageContent())
                                    .owner(found.owner())
                                    .title(found.title())
                                    .build();
                            DocumentContentData documentContentData = assembleDocumentContentData(assembleContentRequest);
                            DocumentPaginationData paginationData = assembleDocumentPaginationData(AssembleDocumentPaginationDataRequest.of(found.totalContentLength(), request.page()));
                            return Ok.of(DocumentAttribute.of(documentContentData, paginationData));
                        }
                        case Err<?> v -> {
                            return Err.of(v.throwable());
                        }
                    }
                })
                .orElseGet(() -> Err.of(new DocumentNotFoundException("")));
    }

    private int insertIntoDatabase(InsertIntoDatabaseRequest request) {
        return documentRepository.create(Document.builder()
                .id(-1)
                .title(request.title())
                .pageContent(request.content())
                .url(request.url())
                .sourceLanguage(request.userDetails().sourceLanguage())
                .targetLanguage(request.userDetails().targetLanguage())
                .owner(request.userDetails().getUsername())
                .totalContentLength(request.content().length())
                .build())
                .orElseThrow(() -> new RuntimeException("Failed to add a new import"));
    }

    private DocumentContentData assembleDocumentContentData(AssembleDocumentContentData request) {
        List<Translation> phrases = translationService.extractPhrases(ExtractPhrasesRequest.of(request.contentBlob(), request.owner()));
        String mappedContent = mapPhrases(MapPhrasesRequest.of(request.contentBlob(), phrases));
        List<Paragraph> content = mapToSelectables(MapToSelectablesRequest.of(mappedContent, new AtomicInteger(0)));
        Map<String, Translation> translations = translationService.findTranslationsInDocument(FindTranslationsInDocumentRequest.of(request.contentBlob(), request.owner()));
        return DocumentContentData.builder()
                .selectedWordId(request.selectableId())
                .documentId(request.documentId())
                .title(request.title())
                .content(content)
                .translations(translations)
                .build();
    }

    public String mapPhrases(MapPhrasesRequest request) {
        List<String> contentAsList = Stream.of(request.contentBlob().split("((?<=\\n)|(?=\\n))| "))
                .map(word -> !word.contains("\n") ? word.trim() : word)
                .filter(word -> !word.isEmpty())
                .toList();
        List<String> formattedContentAsList = Arrays.stream(request.contentBlob().split("((?<=\\n)|(?=\\n))| "))
                .filter(word -> !word.isEmpty())
                .map(word -> !word.contains("\n") ? word.trim() : word)
                .map(String::toLowerCase)
                .map(patternService::removeSpecialCharacters)
                .toList();
        List<PhraseNode> phrasesFound = new ArrayList<>();
        for(Translation phrase : request.phrases()) {
            List<String> searchedPhrase = List.of(patternService.removeSpecialCharacters(phrase.targetWord())
                    .toLowerCase()
                    .split(" "));
            List<WordNode> matchingNodes = IntStream.range(0, contentAsList.size())
                    .mapToObj(i -> WordNode.of(i, formattedContentAsList.get(i), contentAsList.get(i)))
                    .filter(node -> searchedPhrase.contains(node.formattedText()))
                    .distinct()
                    .sorted(Comparator.comparingInt(WordNode::id))
                    .toList();
            List<WordNode> buffer = new ArrayList<>();
            int pointer = 0;
            for(WordNode node : matchingNodes) {
                if(!buffer.isEmpty()) {
                    WordNode lastInBuffer = buffer.getLast();
                    if(node.id() - lastInBuffer.id() != 1) {
                        buffer.clear();
                        pointer = 0;
                    }
                }
                if(!node.formattedText().equals(searchedPhrase.get(pointer))) {
                    buffer.clear();
                    pointer = 0;
                }
                buffer.add(node);
                pointer++;
                String bufferValue = String.join(" ", buffer.stream()
                        .map(WordNode::formattedText)
                        .toList());
                String phraseValue = String.join(" ", searchedPhrase);
                if(bufferValue.equals(phraseValue)) {
                    phrasesFound.add(PhraseNode.fromWordNodes(buffer, phrase));
                    buffer.clear();
                    pointer = 0;
                }
            }
        }
        List<WordNode> phrasesAsWordNodes = phrasesFound.stream()
                .flatMap(phrase -> phrase.wordNodes().stream())
                .sorted(Comparator.comparingInt(WordNode::id))
                .toList();

        List<String> outputContentAsList = new ArrayList<>();
        int wordId = 0;
        int pointer = 0;

        while (wordId < contentAsList.size()) {
            if (pointer < phrasesAsWordNodes.size() && wordId == phrasesAsWordNodes.get(pointer).id()) {
                List<String> phraseParts = new ArrayList<>();
                while (pointer < phrasesAsWordNodes.size() && wordId == phrasesAsWordNodes.get(pointer).id()) {
                    phraseParts.add(phrasesAsWordNodes.get(pointer).renderedText());
                    pointer++;
                    wordId++;
                    if (phraseParts.getLast().endsWith(">>")) {
                        break;
                    }
                }
                outputContentAsList.add(String.join("-", phraseParts));
            } else {
                outputContentAsList.add(contentAsList.get(wordId));
                wordId++;
            }
        }
        return String.join(" ", outputContentAsList);
    }

    private List<Paragraph> mapToSelectables(MapToSelectablesRequest request) {
        return Stream.of(request.contentBlob().split("\n+"))
                .map(paragraphAsString -> Arrays.stream(paragraphAsString.split("\\s+"))
                        .map(selectable -> selectable.startsWith("ph<") && selectable.endsWith(">>") ?
                                Phrase.fromString(selectable, familiarityService.getFamiliarity(selectable), request.idCounter().getAndIncrement()) :
                                (Selectable) Word.of(selectable, request.idCounter().getAndIncrement()))
                        .toList())
                .filter(paragraphAsList -> !paragraphAsList.isEmpty())
                .map(paragraphAsList -> Paragraph.of(paragraphAsList, 0))
                .toList();
    }

    private DocumentPaginationData assembleDocumentPaginationData(AssembleDocumentPaginationDataRequest request) {
        int currentPage = request.currentPage();
        int pagesTotal = paginationService.getNumberOfPages(request.totalLength());
        return DocumentPaginationData.builder()
                .currentPageNumber(currentPage)
                .numberOfPages(pagesTotal)
                .currentRowNumber(paginationService.getCurrentRow(currentPage, paginationService.getMAX_ROW_SIZE()))
                .firstPageOfRowNumber(paginationService.getFirstPageOfRow(currentPage, paginationService.getMAX_ROW_SIZE()))
                .rows(paginationService.getRows(pagesTotal))
                .build();
    }

    public Map<DocumentLocalizationKey, String> getDocumentFormLocalization(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return documentsLocalization.get(principal.userInterfaceLanguage());
    }

    public DocumentFormAttribute getDocumentForm(String strategy, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(principal.userInterfaceLanguage());
        SubmissionStrategy submissionStrategy = switch (strategy) {
            case "url_submit" -> UrlSubmissionStrategy.of("", "", localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
            case "content_submit" -> ContentSubmissionStrategy.of("", "", localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
            default -> throw new IllegalStateException("Unexpected value: " + strategy);
        };
        return DocumentFormAttribute.of(submissionStrategy, localization);
    }
}
