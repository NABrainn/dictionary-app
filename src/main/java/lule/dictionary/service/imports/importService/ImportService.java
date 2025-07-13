package lule.dictionary.service.imports.importService;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.ImportWithId;
import lule.dictionary.dto.database.interfaces.imports.ImportWithPagination;
import lule.dictionary.dto.database.interfaces.imports.base.Import;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.imports.importService.dto.createImportRequest.CreateImportRequest;
import lule.dictionary.service.imports.importService.dto.loadImportPageRequest.LoadImportPageRequest;
import lule.dictionary.service.language.Language;

import java.util.List;

public interface ImportService {
    ServiceResult<Integer> createImport(CreateImportRequest createRequest) throws ConstraintViolationException;
    ServiceResult<List<ImportWithId>> findByOwnerAndTargetLanguage(@NonNull String owner, @NonNull Language targetLanguage);
    ServiceResult<List<Import>> findAll();
    ServiceResult<ImportWithPagination> loadPage(LoadImportPageRequest loadRequest);
}
