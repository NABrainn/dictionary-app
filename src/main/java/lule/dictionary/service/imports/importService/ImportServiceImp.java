package lule.dictionary.service.imports.importService;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.ContentData;
import lule.dictionary.dto.database.implementation.imports.base.ImportImp;
import lule.dictionary.dto.database.interfaces.imports.DocumentWithContent;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.dto.application.result.ServiceResultImp;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importService.dto.importData.ImportData;
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

    public ServiceResult<DocumentContentAttribute> loadDocumentContent(LoadDocumentContentRequest loadRequest) {
        DocumentWithContent documentWithContent = getImport(loadRequest);
        checkPageNumberValidity(loadRequest.page(), getNumberOfPagesForDocument(documentWithContent));
        AssembleDocumentContentRequest request = AssembleDocumentContentRequest.builder()
                .wordId(loadRequest.wordId())
                .importId(loadRequest.importId())
                .page(loadRequest.page())
                .documentWithContent(documentWithContent)
                .totalLength(documentWithContent.totalContentLength())
                .build();
        ImportData importAttribute = createImportData(request);
        PaginationData paginationData = createPaginationData(request);
        return ServiceResultImp.success(DocumentContentAttribute.of(importAttribute, paginationData));
    }

    private DocumentWithContent getImport(LoadDocumentContentRequest loadRequest) throws ImportNotFoundException {
        return importRepository.findById(loadRequest.importId(), loadRequest.page()).orElseThrow(() -> new ImportNotFoundException("Import not found"));
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

    private int getNumberOfPagesForDocument(DocumentWithContent importWithPagination) {
        return paginationService.getNumberOfPages(importWithPagination.totalContentLength());
    }

    private int getNumberOfPagesForLength(int length) {
        return paginationService.getNumberOfPages(length);
    }

    private void checkPageNumberValidity(int page, int numberOfPages) throws InvalidUrlException {
        if(page <= 0 || page > numberOfPages) {
            throw new InvalidUrlException("Invalid url parameter provided");
        }
    }

    private ImportData createImportData(AssembleDocumentContentRequest request) {
        ContentData importContentData = assembleImportContentData(request.documentWithContent());
        Map<String, Translation> importTranslations = getImportTranslationsFromDatabase(request.documentWithContent());
        return ImportData.builder()
                .selectedWordId(request.wordId())
                .importId(request.importId())
                .title(request.documentWithContent().title())
                .content(importContentData)
                .translations(importTranslations)
                .build();
    }

    private ContentData assembleImportContentData(DocumentWithContent importWithPagination) {
        List<List<String>> paragraphs = extractParagraphs(importWithPagination);
        List<Integer> startIndices = extractIndices(paragraphs);
        return ContentData.of(paragraphs, startIndices);
    }

    private Map<String, Translation> getImportTranslationsFromDatabase(DocumentWithContent importWithPagination) {
        return translationService.findTranslationsByImport(FindTranslationsByImportRequest.of(importWithPagination, importWithPagination.owner())).value();
    }

    private List<List<String>> extractParagraphs(DocumentWithContent importWithPagination) {
        return Stream.of(importWithPagination.pageContent().split("\n+"))
                .map(paragraph -> Arrays.stream(paragraph.split("\\s+")).toList())
                .filter(list -> !list.isEmpty())
                .toList();
    }

    private List<Integer> extractIndices(List<List<String>> paragraphs) {
        return IntStream.range(0, paragraphs.size())
                .map(i -> 1 + paragraphs.subList(0, i).stream().mapToInt(List::size).sum())
                .boxed()
                .toList();
    }

    private PaginationData createPaginationData(AssembleDocumentContentRequest request) {
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
