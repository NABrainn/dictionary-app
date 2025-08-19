package lule.dictionary.service.imports.importService;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.ContentData;
import lule.dictionary.dto.database.implementation.imports.base.ImportImp;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.database.interfaces.imports.base.Document;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.imports.dto.ContentSubmission;
import lule.dictionary.service.imports.dto.UrlSubmission;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importService.dto.Phrase;
import lule.dictionary.service.imports.importService.dto.Selectable;
import lule.dictionary.service.imports.importService.dto.Word;
import lule.dictionary.service.imports.importService.dto.importData.DocumentPageData;
import lule.dictionary.service.imports.importService.dto.request.*;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.service.jsoup.JsoupService;
import lule.dictionary.service.pagination.PaginationService;
import lule.dictionary.service.pagination.dto.PaginationData;
import lule.dictionary.service.translation.TranslationService;
import lule.dictionary.service.translation.dto.request.FindTranslationsByImportRequest;
import lule.dictionary.service.validation.ValidationService;
import lule.dictionary.service.validation.ValidationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.InvalidUrlException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImportServiceImp implements ImportService {

    private final ImportRepository importRepository;
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

    public List<ImportWithTranslationData> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request) {
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

    private List<ImportWithTranslationData> getImportByUsernameAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request) {
        return importRepository.findByOwnerAndTargetLanguage(request.owner(), request.targetLanguage());
    }

    private int insertIntoDatabase(InsertIntoDatabaseRequest request) {
        return importRepository.createImport(ImportImp.builder()
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
        String finalContent = content;
        for (Translation translation : phrases) {
            String rawPhrase = translation.targetWord().trim();
            String[] words = rawPhrase.split("\\s+");
            if(words.length > 1) {
                StringBuilder phrase = new StringBuilder(String.join("\\s*", Pattern.quote(words[0]), Pattern.quote(words[1])));
                for (int i = 2; i < words.length; i++) {
                    phrase.append("\\s*").append(Pattern.quote(words[i]));
                }

                String regex = "(?<!\\w|[a-zA-Z0-9])(?:" + phrase + ")(?!\\w|[a-zA-Z0-9])";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

                Matcher matcher = pattern.matcher(finalContent);

                StringBuilder result = new StringBuilder();
                int lastEnd = 0;
                while (matcher.find()) {
                    result.append(finalContent, lastEnd, matcher.start());
                    result.append("ph<")
                            .append(translation.familiarity().ordinal())
                            .append("<")
                            .append(matcher.group().replaceAll("\\s+", "-"))
                            .append(">>");
                    lastEnd = matcher.end();
                }

                result.append(finalContent.substring(lastEnd));
                finalContent = result.toString();
            }
        }
        return finalContent;
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
