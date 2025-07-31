package lule.dictionary.service.imports.importService;

import jakarta.validation.ConstraintViolationException;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.imports.importService.dto.request.CreateImportRequest;
import lule.dictionary.service.imports.importService.dto.request.DocumentContentAttribute;
import lule.dictionary.service.imports.importService.dto.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.service.imports.importService.dto.request.LoadDocumentContentRequest;

import java.util.List;

public interface ImportService {
    ServiceResult<Integer> createImport(CreateImportRequest createRequest) throws ConstraintViolationException;
    ServiceResult<List<ImportWithTranslationData>> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request);
    ServiceResult<DocumentContentAttribute> loadDocumentContent(LoadDocumentContentRequest loadRequest);
}
