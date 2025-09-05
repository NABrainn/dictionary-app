package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentPageData;
import lule.dictionary.pagination.data.PaginationData;

public record DocumentAttribute(@NonNull DocumentPageData importData,
                                @NonNull PaginationData paginationData) {
    public static DocumentAttribute of(DocumentPageData importData, PaginationData paginationData) {
        return new DocumentAttribute(importData, paginationData);
    }
}
