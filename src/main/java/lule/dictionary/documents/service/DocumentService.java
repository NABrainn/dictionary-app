package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.documents.data.request.*;
import lule.dictionary.documents.data.ContentData;
import lule.dictionary.documents.data.entity.Document;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository importRepository;
    private final ValidationService validationService;
    private final PaginationService paginationService;
    private final JsoupService jsoupService;
    private final TranslationService translationService;

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

    public List<DocumentWithTranslationData> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request) {
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
        return importRepository.findById(loadRequest.documentId(), loadRequest.page()).orElseThrow(() -> new ImportNotFoundException("Import not found"));
    }

    private List<DocumentWithTranslationData> getImportByUsernameAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request) {
        return importRepository.findByOwnerAndTargetLanguage(request.owner(), request.targetLanguage());
    }

    private int insertIntoDatabase(InsertIntoDatabaseRequest request) {
        return importRepository.create(Document.builder()
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
//        List<String> contentAsList = List.of(content.split(" "));
//        for(Translation phrase : translationMapper) {
//            contentAsList.stream()
//                    .map(word -> )
//        }
        return "";
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
                                .map(selectable -> selectable instanceof Phrase ?
                                        String.join("", ((Phrase) selectable).targetWords()) :
                                        selectable))
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
