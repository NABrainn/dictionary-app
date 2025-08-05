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
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.dto.application.result.ServiceResultImp;
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
import lule.dictionary.service.userProfile.UserProfileService;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import lule.dictionary.service.validation.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.InvalidUrlException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImportServiceImp implements ImportService {

    private final UserProfileService userProfileService;
    private final ImportRepository importRepository;
    private final ValidationService validationService;
    private final PaginationService paginationService;
    private final JsoupService jsoupService;
    private final TranslationService translationService;

    @Transactional
    public ServiceResult<Integer> createImport(CreateImportRequest request) throws ConstraintViolationException, UserNotFoundException{
        try {
            UserProfile userProfile = getUserProfile(request);
            validate(request);
            int importId = saveImport(request, userProfile);
            return ServiceResultImp.success(importId);
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(e.getMessage(), ServiceResultImp.error(Map.of()));
        }
    }

    public ServiceResult<List<ImportWithTranslationData>> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request) {
        List<ImportWithTranslationData> importList = getImportByUsernameAndTargetLanguage(request);
        return ServiceResultImp.success(importList);
    }

    public ServiceResult<DocumentAttribute> loadDocumentContent(LoadDocumentContentRequest loadRequest) {
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
        return ServiceResultImp.success(DocumentAttribute.of(documentPageData, paginationData));
    }

    private Document getImport(LoadDocumentContentRequest loadRequest) throws ImportNotFoundException {
        return importRepository.findById(loadRequest.documentId(), loadRequest.page()).orElseThrow(() -> new ImportNotFoundException("Import not found"));
    }

    private List<ImportWithTranslationData> getImportByUsernameAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request) {
        return importRepository.findByOwnerAndTargetLanguage(request.owner(), request.targetLanguage());
    }
    private UserProfile getUserProfile(CreateImportRequest addImportRequest) throws UserNotFoundException {
        return userProfileService.getUserProfile(addImportRequest.owner());
    }

    private int saveImport(CreateImportRequest request, UserProfile userProfile) {
        if(!request.content().isEmpty())
            return insertIntoDatabase(InsertIntoDatabaseRequest.of(request, request.content(), userProfile));
        return insertIntoDatabase(InsertIntoDatabaseRequest.of(request, getDocumentContent(request.url()), userProfile));
    }

    private void validate(CreateImportRequest createRequest) throws ConstraintViolationException {
        validationService.validate(createRequest);
    }

    private int insertIntoDatabase(InsertIntoDatabaseRequest insertIntoDatabaseRequest) {
        return importRepository.createImport(ImportImp.builder()
                .title(insertIntoDatabaseRequest.request().title())
                .pageContent(insertIntoDatabaseRequest.content())
                .url(insertIntoDatabaseRequest.request().url())
                .sourceLanguage(insertIntoDatabaseRequest.userProfile().sourceLanguage())
                .targetLanguage(insertIntoDatabaseRequest.userProfile().targetLanguage())
                .owner(insertIntoDatabaseRequest.request().owner())
                .totalContentLength(insertIntoDatabaseRequest.content().length())
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
        return translationService.extractPhrases(content, owner).value();
    }

    //not very efficient
    private String markPhrases(String content, List<Translation> phrases) {
        String finalContent = content;
        for(Translation translation : phrases) {
            finalContent = content
                    .replaceAll("\\b" + translation.targetWord().substring(0, 1).toUpperCase() + translation.targetWord().substring(1) + "\\b", "ph<" + translation.familiarity().ordinal() + 1 + "<" + translation.targetWord().substring(0, 1).toUpperCase() + translation.targetWord().substring(1).replace(" ", "-") + ">>")
                    .replaceAll("\\b" + translation.targetWord().toLowerCase() + "\\b", "ph<" + translation.familiarity().ordinal() + 1 + "<" + translation.targetWord().toLowerCase().replace(" ", "-") + ">>")
                    .replaceAll("\\b" + translation.targetWord().toUpperCase() + "\\b", "ph<" + translation.familiarity().ordinal() + 1 + "<" + translation.targetWord().toUpperCase().replace(" ", "-") + ">>");
        }
        return finalContent;
    }

    private Map<String, Translation> getTranslationsFromDatabase(Document importWithPagination) {
        return translationService.findTranslationsByImport(FindTranslationsByImportRequest.of(importWithPagination, importWithPagination.owner())).value();
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
                .substring(3)
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
