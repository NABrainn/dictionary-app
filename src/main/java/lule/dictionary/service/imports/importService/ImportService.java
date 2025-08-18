package lule.dictionary.service.imports.importService;

import jakarta.validation.ConstraintViolationException;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.imports.importService.dto.request.CreateDocumentRequest;
import lule.dictionary.service.imports.importService.dto.request.DocumentAttribute;
import lule.dictionary.service.imports.importService.dto.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.service.imports.importService.dto.request.LoadDocumentContentRequest;

import java.util.List;

public interface ImportService {
    int createImport(CreateDocumentRequest createRequest) throws ConstraintViolationException;
    List<ImportWithTranslationData> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request);
    DocumentAttribute loadDocumentContent(LoadDocumentContentRequest loadRequest);
}
