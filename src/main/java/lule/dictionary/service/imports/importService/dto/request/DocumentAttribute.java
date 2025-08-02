package lule.dictionary.service.imports.importService.dto.request;

import lombok.NonNull;
import lule.dictionary.service.imports.importService.dto.importData.DocumentPageData;
import lule.dictionary.service.pagination.dto.PaginationData;

public record DocumentAttribute(@NonNull DocumentPageData importData,
                                @NonNull PaginationData paginationData) {
    public static DocumentAttribute of(DocumentPageData importData, PaginationData paginationData) {
        return new DocumentAttribute(importData, paginationData);
    }
}
