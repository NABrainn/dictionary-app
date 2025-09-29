package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.*;
import lule.dictionary.documents.data.attribute.*;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.documents.data.documentSubmission.DocumentFormType;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.documents.data.exception.DocumentServiceException;
import lule.dictionary.documents.data.request.*;
import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.documentSubmission.DocumentFormWithContent;
import lule.dictionary.documents.data.documentSubmission.DocumentFormWithUrl;
import lule.dictionary.documents.data.request.loadDocument.FirstLoadRequest;
import lule.dictionary.documents.data.request.loadDocument.LoadDocumentRequest;
import lule.dictionary.documents.data.request.loadDocument.ReloadWithPhraseRequest;
import lule.dictionary.documents.data.request.loadDocument.ReloadWithWordRequest;
import lule.dictionary.documents.data.result.FirstLoadResult;
import lule.dictionary.documents.data.result.ReloadWithPhraseResult;
import lule.dictionary.documents.data.result.LoadDocumentResult;
import lule.dictionary.documents.data.result.ReloadWithWordResult;
import lule.dictionary.familiarity.service.FamiliarityService;
import lule.dictionary.jsoup.data.Token;
import lule.dictionary.language.service.Language;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.stringUtil.service.StringUtils;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.documents.data.repository.DocumentRepository;
import lule.dictionary.jsoup.service.JsoupService;
import lule.dictionary.pagination.service.PaginationService;
import lule.dictionary.pagination.data.DocumentPaginationData;
import lule.dictionary.translations.data.request.ExtractPhrasesRequest;
import lule.dictionary.translations.data.request.FindTranslationsInDocumentRequest;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.userProfiles.service.UserInterfaceService;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.rule.NotEmpty;
import lule.dictionary.validation.data.ValidationException;
import lule.dictionary.validation.data.rule.Size;
import lule.dictionary.validation.service.Validator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    private final UserInterfaceService userInterfaceService;
    private final StringUtils stringUtils;

    @Transactional
    public Result<Integer> createDocument(CreateDocumentRequest request) {
        UserProfile principal = (UserProfile) request.authentication().getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(principal.userInterfaceLanguage());
        DocumentFormType documentFormType = switch (request.documentFormType()) {
            case "url_form" -> DocumentFormWithUrl.of(request.title(), request.url(), localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
            case "content_form" -> DocumentFormWithContent.of(request.title(), request.content(), localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
            default -> throw new IllegalStateException("Unexpected value: " + request.documentFormType());
        };
        return switch (documentFormType) {
            case DocumentFormWithUrl documentFormWithUrl -> {
                Result<?> result = validator.validate(
                        Constraint.of("title", NotEmpty.of(request.title()), switch (uiLanguage) {
                            case PL -> "Tytuł nie może być pusty";
                            case EN -> "Title cannot be empty";
                            case IT -> "Il titolo non può essere vuoto";
                            case NO -> "Tittelen kan ikke være tom";
                        }),
                        Constraint.of("title", Size.of(request.title(), 0, 500), switch (uiLanguage) {
                            case PL -> "Tytuł nie może być dłuższy niż 500 znaków";
                            case EN -> "Title cannot be longer than 500 characters";
                            case IT -> "Il titolo non può essere più lungo di 500 caratteri";
                            case NO -> "Tittelen kan ikke være lenger enn 500 tegn";
                        }),
                        Constraint.of("url", NotEmpty.of(request.url()), switch (uiLanguage) {
                            case PL -> "URL nie może być pusty";
                            case EN -> "URL cannot be empty";
                            case IT -> "L'URL non può essere vuoto";
                            case NO -> "URL-en kan ikke være tom";
                        }),
                        Constraint.of("url", Size.of(request.url(), 0, 500), switch (uiLanguage) {
                            case PL -> "URL nie może być dłuższy niż 500 znaków";
                            case EN -> "URL cannot be longer than 500 characters";
                            case IT -> "L'URL non può essere più lungo di 500 caratteri";
                            case NO -> "URL-en kan ikke være lenger enn 500 tegn";
                        })
                );
                yield switch (result) {
                    case Ok<?> ignored -> {
                        String[] documentAsArray = jsoupService.fetchDocument(documentFormWithUrl.url())
                                .wholeText()
                                .split("(?<=\\n)(?=\\w)");
                        String formattedDocument = Arrays.stream(documentAsArray)
                                .map(tokenBlob -> Token.of(tokenBlob, stringUtils.getCharQuantity(tokenBlob, '\n')))
                                .map(token -> switch (token.newlineCount()) {
                                    case 0 -> token;
                                    case 1 -> token.withContent(patternService.replaceNewline(token.content(), " "));
                                    default -> token.withContent(patternService.replaceAllNewlines(token.content(), '\n', 30));
                                })
                                .map(Token::content)
                                .filter(Predicate.not(String::isBlank))
                                .collect(Collectors.joining());
                        Document document = Document.builder()
                                .id(-1)
                                .title(documentFormWithUrl.title())
                                .pageContent(formattedDocument)
                                .url(documentFormWithUrl.url())
                                .sourceLanguage(principal.sourceLanguage())
                                .targetLanguage(principal.targetLanguage())
                                .owner(principal.getUsername())
                                .totalContentLength(formattedDocument.length())
                                .build();
                        int documentId = documentRepository.create(document).orElseThrow();
                        yield Ok.of(documentId);
                    }
                    case Err<?> v -> v.throwable() instanceof ValidationException validationException ?
                            Err.of(new DocumentServiceException(DocumentFormAttribute.of(documentFormType, localization), validationException.getViolations())) :
                            Err.of(new RuntimeException());
                };

            }
            case DocumentFormWithContent contentSubmission -> {
                Result<?> result = validator.validate(
                        Constraint.of("title", NotEmpty.of(request.title()), switch (uiLanguage) {
                            case PL -> "Tytuł nie może być pusty";
                            case EN -> "Title cannot be empty";
                            case IT -> "Il titolo non può essere vuoto";
                            case NO -> "Tittelen kan ikke være tom";
                        }),
                        Constraint.of("title", Size.of(request.title(), 0, 500), switch (uiLanguage) {
                            case PL -> "Tytuł nie może być dłuższy niż 500 znaków";
                            case EN -> "Title cannot be longer than 500 characters";
                            case IT -> "Il titolo non può essere più lungo di 500 caratteri";
                            case NO -> "Tittelen kan ikke være lenger enn 500 tegn";
                        }),
                        Constraint.of("content", NotEmpty.of(request.content()), switch (uiLanguage) {
                            case PL -> "Treść nie może być pusta";
                            case EN -> "Content cannot be empty";
                            case IT -> "Il contenuto non può essere vuoto";
                            case NO -> "Innholdet kan ikke være tomt";
                        }),
                        Constraint.of("content", Size.of(request.content(), 0, 100000), switch (uiLanguage) {
                            case PL -> "Treść nie może być dłuższa niż 100 000 znaków";
                            case EN -> "Content cannot be longer than 100,000 characters";
                            case IT -> "Il contenuto non può essere più lungo di 100.000 caratteri";
                            case NO -> "Innholdet kan ikke være lenger enn 100 000 tegn";
                        })
                );
                yield switch (result) {
                    case Ok<?> ignored -> {
                        String content = contentSubmission.content();
                        Document document = Document.builder()
                                .id(-1)
                                .title(contentSubmission.title())
                                .pageContent(content)
                                .url("")
                                .sourceLanguage(principal.sourceLanguage())
                                .targetLanguage(principal.targetLanguage())
                                .owner(principal.getUsername())
                                .totalContentLength(content.length())
                                .build();
                        int documentId = documentRepository.create(document).orElseThrow();
                        yield Ok.of(documentId);
                    }
                    case Err<?> v -> v.throwable() instanceof ValidationException validationException ?
                            Err.of(new DocumentServiceException(DocumentFormAttribute.of(documentFormType, localization), validationException.getViolations())) :
                            Err.of(new RuntimeException());
                };
            }
        };
    }

    public DocumentListAttribute findMany(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        Language targetLanguage = principal.targetLanguage();
        String username = principal.getUsername();

        List<DocumentWithTranslationData> documents = documentRepository.findByOwnerAndTargetLanguage(username, targetLanguage);
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(uiLanguage);
        boolean isNavbarOpen = userInterfaceService.isNavbarToggled(authentication);
        return DocumentListAttribute.of(documents, localization, isNavbarOpen);
    }

    public Result<LoadDocumentResult> loadDocumentContent(LoadDocumentRequest request, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language sourceLanguage = principal.sourceLanguage();
        Language targetLanguage = principal.targetLanguage();
        Result<Document> result = documentRepository.findById(request.documentId(), request.page())
                .map(found -> documentSanitizer.validateNumberOfPages(SanitizeNumberOfPagesRequest.of(request.page(), paginationService.getNumberOfPages(found.totalContentLength()), found)))
                .orElseThrow();
        return switch (result) {
            case Ok<Document> ok -> {
                Document document = ok.value();
                Map<String, Translation> translations = translationService.findTranslations(FindTranslationsInDocumentRequest.of(document.pageContent(), document.owner()));
                Phrases phrases = Phrases.of(translationService.findPhrases(ExtractPhrasesRequest.of(document.pageContent(), document.owner())));
                DocumentUnitStore processedContent = Arrays.stream(document.pageContent().split("\\s+"))
                        .sequential()
                        .map(word -> switch (translations.get(patternService.removeSpecialCharacters(word.toLowerCase().trim()))) {
                            case Translation translation -> TranslationUnit.of(translation, word, phrases.containsWord(translation.processedTargetWord()));
                            case null -> WordUnit.of(Translation.builder()
                                    .sourceWords(List.of())
                                    .processedTargetWord(patternService.removeSpecialCharacters(word.toLowerCase().trim()))
                                    .familiarity(Familiarity.UNKNOWN)
                                    .sourceLanguage(sourceLanguage)
                                    .targetLanguage(targetLanguage)
                                    .owner(document.owner())
                                    .isPhrase(false)
                                    .build(),
                                    word,
                                    phrases.containsWord(word.toLowerCase().trim()));
                            })
                        .map(unit -> (DocumentUnit) unit)
                        .collect(Collector.of(
                                () -> DocumentUnitStore.of(new ArrayList<>(), new ArrayList<>()),
                                (store, documentUnit) -> {
                                    if (!documentUnit.isPhrasePart()) {
                                        store.addDocumentUnit(documentUnit);
                                        store.clearPhraseParts();
                                    }
                                    else {
                                        store.addDocumentUnit(documentUnit);
                                        store.addPhrasePart(documentUnit);
                                    }
                                    if (phrases.findPhrase(store.bufferValue()).isPresent()) {
                                        Translation translation = phrases.findPhrase(store.bufferValue()).get();
                                        store.wrapToPhrase(translation);
                                        store.clearPhraseParts();
                                    }
                                },
                                (left, right) -> {
                                    List<DocumentUnit> leftUnits = left.documentUnits();
                                    List<DocumentUnit> rightUnits = right.documentUnits();
                                    leftUnits.addAll(rightUnits);
                                    return left;
                                },
                                Collector.Characteristics.IDENTITY_FINISH
                        ));
                DocumentContentData contentData = DocumentContentData.builder()
                        .title(document.title())
                        .content(processedContent.documentUnits())
                        .translations(translations)
                        .documentId(request.documentId())
                        .build();
                DocumentPaginationData paginationData = DocumentPaginationData.builder()
                        .currentPageNumber(request.page())
                        .numberOfPages(paginationService.getNumberOfPages(document.totalContentLength()))
                        .currentRowNumber(paginationService.getCurrentRow(request.page(), paginationService.getMAX_ROW_SIZE()))
                        .firstPageOfRowNumber(paginationService.getFirstPageOfRow(request.page(), paginationService.getMAX_ROW_SIZE()))
                        .rows(paginationService.getRows(paginationService.getNumberOfPages(document.totalContentLength())))
                        .build();
                boolean isNavbarOpen = userInterfaceService.hideNavbar(authentication);
                yield switch (request) {
                    case FirstLoadRequest ignored -> Ok.of(FirstLoadResult.builder()
                            .documentContentData(contentData)
                            .paginationData(paginationData)
                            .isNavbarOpen(isNavbarOpen)
                            .build());
                    case ReloadWithWordRequest wordRequest -> Ok.of(ReloadWithWordResult.builder()
                            .documentContentData(contentData)
                            .paginationData(paginationData)
                            .isNavbarOpen(isNavbarOpen)
                            .unitId(wordRequest.unitId())
                            .targetWord(request.unitText())
                            .isSelectablePersisted(request.isUnitPersisted())
                            .build());
                    case ReloadWithPhraseRequest phraseRequest -> Ok.of(ReloadWithPhraseResult.builder()
                            .documentContentData(contentData)
                            .paginationData(paginationData)
                            .isNavbarOpen(isNavbarOpen)
                            .startId(phraseRequest.startId())
                            .endId(phraseRequest.endId())
                            .targetWord(request.unitText())
                            .isSelectablePersisted(request.isUnitPersisted())
                            .build());
                };
            }
            case Err<Document> v -> Err.of(v.throwable());
        };
    }

    public Map<DocumentLocalizationKey, String> getDocumentFormLocalization(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return documentsLocalization.get(principal.userInterfaceLanguage());
    }

    public DocumentFormAttribute getDocumentForm(String strategy, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(principal.userInterfaceLanguage());
        DocumentFormType documentFormType = switch (strategy) {
            case "url_form" -> DocumentFormWithUrl.of("", "", localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
            case "content_form" -> DocumentFormWithContent.of("", "", localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
            default -> throw new IllegalStateException("Unexpected value: " + strategy);
        };
        return DocumentFormAttribute.of(documentFormType, localization);
    }
}
