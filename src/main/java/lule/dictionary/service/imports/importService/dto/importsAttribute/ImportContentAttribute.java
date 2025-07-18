package lule.dictionary.service.imports.importService.dto.importsAttribute;

import lombok.NonNull;
import lule.dictionary.service.imports.importService.dto.importData.ImportAttribute;
import lule.dictionary.service.pagination.dto.PaginationData;

public record ImportContentAttribute(@NonNull ImportAttribute importData,
                                     @NonNull PaginationData paginationData) {
    public static ImportContentAttribute of(ImportAttribute importData, PaginationData paginationData) {
        return new ImportContentAttribute(importData, paginationData);
    }
}
