package lule.dictionary.service.imports.importService.dto.importsAttribute;

import lombok.NonNull;
import lule.dictionary.service.imports.importService.dto.importData.ImportData;
import lule.dictionary.service.pagination.dto.PaginationData;

public record ImportContentAttribute(@NonNull ImportData importData,
                                     @NonNull PaginationData paginationData) {
    public static ImportContentAttribute of(ImportData importData, PaginationData paginationData) {
        return new ImportContentAttribute(importData, paginationData);
    }
}
