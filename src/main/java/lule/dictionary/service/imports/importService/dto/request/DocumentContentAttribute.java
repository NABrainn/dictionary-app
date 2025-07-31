package lule.dictionary.service.imports.importService.dto.request;

import lombok.NonNull;
import lule.dictionary.service.imports.importService.dto.importData.ImportData;
import lule.dictionary.service.pagination.dto.PaginationData;

public record DocumentContentAttribute(@NonNull ImportData importData,
                                       @NonNull PaginationData paginationData) {
    public static DocumentContentAttribute of(ImportData importData, PaginationData paginationData) {
        return new DocumentContentAttribute(importData, paginationData);
    }
}
