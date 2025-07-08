package lule.dictionary.service.imports;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.imports.base.ImportImp;
import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.exception.RetryViewException;
import lule.dictionary.service.dto.request.factory.AbstractServiceRequestFactory;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.dto.request.InsertIntoDatabaseRequest;
import lule.dictionary.service.imports.dto.request.LoadImportPageRequest;
import lule.dictionary.service.imports.dto.request.CreateImportRequest;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.service.pagination.PaginationService;
import lule.dictionary.service.userProfile.UserProfileService;
import lule.dictionary.service.validation.ValidationService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.util.InvalidUrlException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final UserProfileService userProfileService;
    private final ImportRepository importRepository;
    private final ValidationService validationService;
    private final PaginationService paginationService;
    private final AbstractServiceRequestFactory requestFactory;

    @Transactional
    public int createImport(CreateImportRequest createRequest) throws ConstraintViolationException {
        UserProfile userProfile = getUserProfile(createRequest);
        validate(createRequest);
        return saveImport(createRequest, createRequest, userProfile);
    }

    public ImportWithPagination getImport(LoadImportPageRequest loadRequest) throws ImportNotFoundException {
        return getImportById(loadRequest);
    }


    public List<ImportWithId> findByOwner(@NonNull Model model, @NonNull String owner) {
        return getImportByUsername(owner);
    }


    public List<Import> findAll() {
        return getAllImports();
    }

    public ImportWithPagination loadPage(LoadImportPageRequest loadRequest) {
        ImportWithPagination importWithPagination = getImport(loadRequest);
        checkPageNumberValidity(loadRequest.page(), getNumberOfPages(importWithPagination));
        return importWithPagination;
    }

    private List<Import> getAllImports() {
        return importRepository.findAll();
    }
    private List<ImportWithId> getImportByUsername(String owner) {
        return importRepository.findByOwner(owner);
    }
    private ImportWithPagination getImportById(LoadImportPageRequest loadRequest) {
        return importRepository.findById(loadRequest.importId(), loadRequest.page()).orElseThrow(() -> new ImportNotFoundException("Import not found"));
    }
    private UserProfile getUserProfile(CreateImportRequest addImportRequest) {
        return userProfileService.getUserProfile(addImportRequest.owner());
    }

    private int saveImport(CreateImportRequest createRequest, CreateImportRequest validRequest, UserProfile userProfile) {
        if(!createRequest.content().isEmpty())
            return insertIntoDatabase(requestFactory.newInsertIntoDatabaseRequest(validRequest, validRequest.content(), userProfile));
        return insertIntoDatabase(requestFactory.newInsertIntoDatabaseRequest(validRequest, getDocumentContent(validRequest.url()), userProfile));
    }

    private void validate(CreateImportRequest createRequest) {
        validationService.validate(createRequest);
    }

    private int insertIntoDatabase(InsertIntoDatabaseRequest insertIntoDatabaseRequest) {
        return importRepository.createImport(ImportImp.builder()
                .title(insertIntoDatabaseRequest.validRequest().title())
                .content(insertIntoDatabaseRequest.content())
                .url(insertIntoDatabaseRequest.validRequest().url())
                .sourceLanguage(insertIntoDatabaseRequest.userProfile().sourceLanguage())
                .targetLanguage(insertIntoDatabaseRequest.userProfile().targetLanguage())
                .owner(insertIntoDatabaseRequest.validRequest().owner())
                .build()).orElseThrow(() -> new RetryViewException("Failed to add a new import"));
    }

    private String getDocumentContent(String url) {
            Document document = getDocument(url);
            return document.text();
    }

    private Document getDocument(String url) {
        try {
            return Jsoup.connect(prependHttpFormat(url)).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String prependHttpFormat(String url) {
        if(!url.startsWith("https://") && !url.startsWith("http://")) {
            return "https://".concat(url);
        }
        return url;
    }

    private int getNumberOfPages(ImportWithPagination importWithPagination) {
        return paginationService.getNumberOfPages(importWithPagination.content().length());
    }

    private void checkPageNumberValidity(int page, int numberOfPages) throws InvalidUrlException {
        if(page <= 0 || page > numberOfPages) {
            throw new InvalidUrlException("Invalid url parameter provided");
        }
    }

}
