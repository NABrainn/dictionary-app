package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.DocumentPaginationData;

public record DocumentAttribute(@NonNull DocumentContentData importData,
                                @NonNull DocumentPaginationData paginationData) {
    public static DocumentAttribute of(DocumentContentData importData, DocumentPaginationData paginationData) {
        return new DocumentAttribute(importData, paginationData);
    }
}
