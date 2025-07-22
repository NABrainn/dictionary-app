package lule.dictionary.service.imports.importService;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.ImportWithId;
import lule.dictionary.dto.database.interfaces.imports.ImportWithPagination;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.database.interfaces.imports.base.Import;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.imports.importService.dto.request.CreateImportRequest;
import lule.dictionary.service.imports.importService.dto.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.service.imports.importService.dto.request.LoadImportPageRequest;
import lule.dictionary.service.language.Language;

import java.util.List;

public interface ImportService {
    ServiceResult<Integer> createImport(CreateImportRequest createRequest) throws ConstraintViolationException;
    ServiceResult<List<ImportWithTranslationData>> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request);
    ServiceResult<ImportWithPagination> loadPage(LoadImportPageRequest loadRequest);
}
