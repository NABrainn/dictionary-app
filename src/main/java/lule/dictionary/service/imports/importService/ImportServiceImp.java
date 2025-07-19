package lule.dictionary.service.imports.importService;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.database.implementation.imports.base.ImportImp;
import lule.dictionary.dto.database.interfaces.imports.ImportWithPagination;
import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.dto.application.result.ServiceResultImp;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importService.dto.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.service.imports.importService.dto.request.InsertIntoDatabaseRequest;
import lule.dictionary.service.imports.importService.dto.request.LoadImportPageRequest;
import lule.dictionary.service.imports.importService.dto.request.CreateImportRequest;
import lule.dictionary.dto.database.interfaces.imports.base.Import;
import lule.dictionary.dto.database.interfaces.imports.ImportWithId;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.service.jsoup.JsoupService;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.pagination.PaginationService;
import lule.dictionary.service.userProfile.UserProfileService;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import lule.dictionary.service.validation.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.InvalidUrlException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImportServiceImp implements ImportService {

    private final UserProfileService userProfileService;
    private final ImportRepository importRepository;
    private final ValidationService validationService;
    private final PaginationService paginationService;
    private final JsoupService jsoupService;


    @Transactional
    public ServiceResult<Integer> createImport(CreateImportRequest request) throws ConstraintViolationException, UserNotFoundException{
        try {
            UserProfile userProfile = getUserProfile(request);
            validate(request);
            int importId = saveImport(request, userProfile);
            return ServiceResultImp.success(importId);
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(e.getMessage(), ServiceResultImp.errorEmpty(Map.of()));
        }
    }

    public ServiceResult<List<ImportWithId>> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request) {
        List<ImportWithId> importList = getImportByUsernameAndTargetLanguage(request);
        return ServiceResultImp.success(importList);
    }

    public ServiceResult<ImportWithPagination> loadPage(LoadImportPageRequest loadRequest) {
        ImportWithPagination importWithPagination = getImport(loadRequest);
        checkPageNumberValidity(loadRequest.page(), getNumberOfPages(importWithPagination));
        return ServiceResultImp.success(importWithPagination);
    }

    private ImportWithPagination getImport(LoadImportPageRequest loadRequest) throws ImportNotFoundException {
        return importRepository.findById(loadRequest.importId(), loadRequest.page()).orElseThrow(() -> new ImportNotFoundException("Import not found"));
    }

    private List<ImportWithId> getImportByUsernameAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request) {
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
        System.out.println("leng: " + insertIntoDatabaseRequest.request().content().length());
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

    private int getNumberOfPages(ImportWithPagination importWithPagination) {
        return paginationService.getNumberOfPages(importWithPagination.totalContentLength());
    }

    private void checkPageNumberValidity(int page, int numberOfPages) throws InvalidUrlException {
        if(page <= 0 || page > numberOfPages) {
            throw new InvalidUrlException("Invalid url parameter provided");
        }
    }

}
