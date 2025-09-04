package lule.dictionary.documents.service.dto.request;

import lombok.NonNull;
import lule.dictionary.documents.service.dto.DocumentPageData;
import lule.dictionary.pagination.service.dto.PaginationData;

public record DocumentAttribute(@NonNull DocumentPageData importData,
                                @NonNull PaginationData paginationData) {
    public static DocumentAttribute of(DocumentPageData importData, PaginationData paginationData) {
        return new DocumentAttribute(importData, paginationData);
    }
}
