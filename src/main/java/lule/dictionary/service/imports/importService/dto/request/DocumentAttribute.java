package lule.dictionary.service.imports.importService.dto.request;

import lombok.NonNull;
import lule.dictionary.service.imports.importService.dto.importData.ContentData;
import lule.dictionary.service.pagination.dto.PaginationData;

public record DocumentAttribute(@NonNull ContentData importData,
                                @NonNull PaginationData paginationData) {
    public static DocumentAttribute of(ContentData importData, PaginationData paginationData) {
        return new DocumentAttribute(importData, paginationData);
    }
}
