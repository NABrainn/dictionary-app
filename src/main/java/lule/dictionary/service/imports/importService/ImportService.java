package lule.dictionary.service.imports.importService;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.service.dto.result.ServiceResult;
import lule.dictionary.service.imports.importService.dto.createImportRequest.CreateImportRequest;
import lule.dictionary.service.imports.importService.dto.loadImportPageRequest.LoadImportPageRequest;

import java.util.List;

public interface ImportService {
    ServiceResult<Integer> createImport(CreateImportRequest createRequest) throws ConstraintViolationException;
    ServiceResult<List<ImportWithId>> findByOwner(@NonNull String owner);
    ServiceResult<List<Import>> findAll();
    ServiceResult<ImportWithPagination> loadPage(LoadImportPageRequest loadRequest);
}
