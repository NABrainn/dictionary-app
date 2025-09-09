package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.*;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.documents.data.request.*;
import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.strategy.ContentSubmissionStrategy;
import lule.dictionary.documents.data.strategy.UrlSubmissionStrategy;
import lule.dictionary.familiarity.FamiliarityService;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.documents.service.exception.ImportNotFoundException;
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
import lule.dictionary.validation.service.ValidationService;
import lule.dictionary.validation.service.ValidationServiceException;
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
    private final ValidationService validationService;
    private final PaginationService paginationService;
    private final JsoupService jsoupService;
    private final TranslationService translationService;
    private final PatternService patternService;
    private final FamiliarityService familiarityService;
    private final DocumentSanitizer documentSanitizer;

    @Transactional
    public int createDocument(CreateDocumentRequest request) throws ValidationServiceException {
        switch (request.submissionStrategy()) {
            case UrlSubmissionStrategy urlSubmission -> {
                validationService.validate(urlSubmission);
                String content = jsoupService.importDocumentContent(urlSubmission.url());
                return insertIntoDatabase(InsertIntoDatabaseRequest.builder()
                        .title(urlSubmission.title())
                        .url(urlSubmission.url())
                        .content(content)
                        .userDetails(request.userDetails())
                        .build());
            }
            case ContentSubmissionStrategy contentSubmission -> {
                validationService.validate(contentSubmission);
                String content = contentSubmission.content();
                return insertIntoDatabase(InsertIntoDatabaseRequest.builder()
                        .title(contentSubmission.title())
                        .url("")
                        .content(content)
                        .userDetails(request.userDetails())
                        .build());
            }
            default -> throw new IllegalStateException("Unexpected value: " + request.submissionStrategy());
        }
    }

    public List<DocumentWithTranslationData> findByOwnerAndTargetLanguage(FindByTargetLanguageRequest request) {
        return documentRepository.findByOwnerAndTargetLanguage(request.owner(), request.targetLanguage());
    }

    public DocumentAttribute loadDocumentContent(LoadDocumentContentRequest request) {
        Document document = documentRepository.findById(request.documentId(), request.page())
                .orElseThrow(() -> new ImportNotFoundException("Import not found"));
        documentSanitizer.sanitizeNumberOfPages(SanitizeNumberOfPagesRequest.of(request.page(), paginationService.getNumberOfPages(document.totalContentLength())));
        AssembleDocumentContentData assembleContentRequest = AssembleDocumentContentData.builder()
                .selectableId(request.wordId())
                .documentId(request.documentId())
                .contentBlob(document.pageContent())
                .owner(document.owner())
                .title(document.title())
                .build();
        DocumentContentData documentContentData = assembleDocumentContentData(assembleContentRequest);
        DocumentPaginationData paginationData = assembleDocumentPaginationData(AssembleDocumentPaginationDataRequest.of(document.totalContentLength(), request.page()));
        return DocumentAttribute.of(documentContentData, paginationData);
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
                .build()).orElseThrow(() -> new RuntimeException("Failed to add a new import"));
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
//        IntStream.range(0, formattedContentAsList.size()).forEach(id -> System.out.println("formatted: " + id + ": " + formattedContentAsList.get(id) + "; content: " + id + ": " + contentAsList.get(id) + ";"));
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
                    phrasesFound.add(PhraseNode.of(List.copyOf(buffer.stream()
                            .map(wordNode -> buffer.getFirst().equals(wordNode) ?
                                    wordNode.copyWithRenderedText("ph<" + (phrase.familiarity().ordinal()) + "<" + wordNode.renderedText()) :
                                    buffer.getLast().equals(wordNode) ?
                                            wordNode.copyWithRenderedText(wordNode.renderedText() + ">>") :
                                            wordNode
                            )
                            .toList())));
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
                                Phrase.process(selectable, familiarityService.getFamiliarity(selectable), request.idCounter().getAndIncrement()) :
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
}
