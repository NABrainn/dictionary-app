package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.PhraseNode;
import lule.dictionary.documents.data.WordNode;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.documents.data.request.*;
import lule.dictionary.documents.data.ContentData;
import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.documents.data.strategy.ContentSubmission;
import lule.dictionary.documents.data.strategy.UrlSubmission;
import lule.dictionary.documents.service.exception.ImportNotFoundException;
import lule.dictionary.documents.data.selectable.Phrase;
import lule.dictionary.documents.data.selectable.Selectable;
import lule.dictionary.documents.data.selectable.Word;
import lule.dictionary.documents.data.DocumentPageData;
import lule.dictionary.documents.data.repository.DocumentRepository;
import lule.dictionary.jsoup.service.JsoupService;
import lule.dictionary.pagination.service.PaginationService;
import lule.dictionary.pagination.data.PaginationData;
import lule.dictionary.translations.data.request.FindTranslationsByImportRequest;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.validation.service.ValidationService;
import lule.dictionary.validation.service.ValidationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.InvalidUrlException;

import java.util.*;
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

    @Transactional
    public int createImport(CreateDocumentRequest request) throws ValidationServiceException {
        switch (request.submissionStrategy()) {
            case UrlSubmission urlSubmission -> {
                validationService.validate(urlSubmission);
                String content = getDocumentContent(urlSubmission.url());
                return insertIntoDatabase(InsertIntoDatabaseRequest.builder()
                        .title(urlSubmission.title())
                        .url(urlSubmission.url())
                        .content(content)
                        .userDetails(request.userDetails())
                        .build());
            }
            case ContentSubmission contentSubmission -> {
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
        return getImportByUsernameAndTargetLanguage(request);
    }

    public DocumentAttribute loadDocumentContent(LoadDocumentContentRequest loadRequest) {
        Document document = getImport(loadRequest);
        validatePageNumber(loadRequest.page(), getNumberOfPagesForDocument(document));
        AssembleDocumentAttributeRequest assembleRequest = AssembleDocumentAttributeRequest.builder()
                .wordId(loadRequest.wordId())
                .documentId(loadRequest.documentId())
                .page(loadRequest.page())
                .document(document)
                .totalLength(document.totalContentLength())
                .build();
        DocumentPageData documentPageData = createDocumentPageData(assembleRequest);
        PaginationData paginationData = createPaginationData(assembleRequest);
        return DocumentAttribute.of(documentPageData, paginationData);
    }

    private Document getImport(LoadDocumentContentRequest loadRequest) throws ImportNotFoundException {
        return documentRepository.findById(loadRequest.documentId(), loadRequest.page()).orElseThrow(() -> new ImportNotFoundException("Import not found"));
    }

    private List<DocumentWithTranslationData> getImportByUsernameAndTargetLanguage(FindByTargetLanguageRequest request) {
        return documentRepository.findByOwnerAndTargetLanguage(request.owner(), request.targetLanguage());
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

    private String getDocumentContent(String url) {
        return jsoupService.importDocumentContent(url);
    }

    private int getNumberOfPagesForDocument(Document document) {
        return paginationService.getNumberOfPages(document.totalContentLength());
    }

    private int getNumberOfPagesForLength(int length) {
        return paginationService.getNumberOfPages(length);
    }

    private void validatePageNumber(int page, int numberOfPages) throws InvalidUrlException {
        if(page <= 0 || page > numberOfPages) {
            throw new InvalidUrlException("Invalid url parameter provided");
        }
    }

    private DocumentPageData createDocumentPageData(AssembleDocumentAttributeRequest request) {
        ContentData documentContentData = assembleDocumentContentData(request.document());
        Map<String, Translation> translations = getTranslationsFromDatabase(request.document());
        return DocumentPageData.builder()
                .selectedWordId(request.wordId())
                .documentId(request.documentId())
                .title(request.document().title())
                .content(documentContentData)
                .translations(translations)
                .build();
    }

    private ContentData assembleDocumentContentData(Document document) {
        List<Translation> phrases = extractPhrases(document.pageContent(), document.owner());
        String parsedContent = markPhrases(document.pageContent(), phrases.stream().toList());
        List<List<Selectable>> paragraphs = extractParagraphs(parsedContent);
        List<Integer> startIndices = extractIndices(paragraphs);
        return ContentData.of(paragraphs, startIndices);
    }

    private List<Translation> extractPhrases(String content, String owner) {
        return translationService.extractPhrases(content, owner);
    }

    public String markPhrases(String content, List<Translation> phrases) {
        List<String> contentAsList = Stream.of(content.split("((?<=\\n)|(?=\\n))| "))
                .map(word -> !word.contains("\n") ? word.trim() : word)
                .filter(word -> !word.isEmpty())
                .toList();
        List<String> formattedContentAsList = Arrays.stream(content.split("((?<=\\n)|(?=\\n))| "))
                .filter(word -> !word.isEmpty())
                .map(word -> !word.contains("\n") ? word.trim() : word)
                .map(String::toLowerCase)
                .map(patternService::removeSpecialCharacters)
                .toList();
        IntStream.range(0, formattedContentAsList.size()).forEach(id -> System.out.println("formatted: " + id + ": " + formattedContentAsList.get(id) + "; content: " + id + ": " + contentAsList.get(id) + ";"));
        List<PhraseNode> phrasesFound = new ArrayList<>();
        for(Translation phrase : phrases) {
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
                                            wordNode)
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

    private Map<String, Translation> getTranslationsFromDatabase(Document importWithPagination) {
        return translationService.findTranslationsByImport(FindTranslationsByImportRequest.of(importWithPagination, importWithPagination.owner()));
    }
    private List<List<Selectable>> extractParagraphs(String content) {
        return Stream.of(content.split("\n+"))
                .map(paragraph -> Arrays.stream(paragraph.split("\\s+"))
                        .map(selectable -> selectable.startsWith("ph<") && selectable.endsWith(">") ?
                                buildPhraseFrom(selectable) :
                                (Selectable) Word.of(selectable))
                        .toList())
                .filter(list -> !list.isEmpty())
                .toList();
    }

    private Phrase buildPhraseFrom(String literal) {
        return Phrase.of(Arrays.stream(literal
                .replace("ph<", "")
                .replace(">", "")
                .replace("-", " ")
                .substring(2)
                .split(" "))
                .toList(), Familiarity.values()[Integer.parseInt(literal.substring(3, 4))]);
    }

    private List<Integer> extractIndices(List<List<Selectable>> paragraphs) {
        return IntStream.range(0, paragraphs.stream()
                .map(paragraph -> paragraph.stream()
                    .map(selectable -> selectable instanceof Phrase ? String.join("", ((Phrase) selectable).targetWords()) : selectable))
                .toList()
                .size())
                .map(i -> 1 + paragraphs.subList(0, i).stream()
                    .mapToInt(List::size)
                    .sum())
                .boxed()
                .toList();
    }

    private PaginationData createPaginationData(AssembleDocumentAttributeRequest request) {
        int currentPage = request.page();
        int pagesTotal = getNumberOfPagesForLength(request.totalLength());
        return PaginationData.builder()
                .currentPageNumber(currentPage)
                .numberOfPages(pagesTotal)
                .currentRowNumber(getNumberOfCurrentRow(currentPage))
                .firstPageOfRowNumber(getNumberOfRowFirstPage(currentPage))
                .rows(getRows(pagesTotal))
                .build();
    }

    private int getNumberOfCurrentRow(int page) {
        return paginationService.getCurrentRow(page, paginationService.getMAX_ROW_SIZE());
    }

    private int getNumberOfRowFirstPage(int page) {
        return paginationService.getFirstPageOfRow(page, paginationService.getMAX_ROW_SIZE());
    }

    private List<List<Integer>> getRows(int pagesTotal) {
        return paginationService.getRows(pagesTotal);
    }
}
