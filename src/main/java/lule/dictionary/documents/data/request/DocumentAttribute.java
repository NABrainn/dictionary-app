package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.DocumentPaginationData;

public record DocumentAttribute(@NonNull DocumentContentData documentContentData,
                                @NonNull DocumentPaginationData paginationData,
                                boolean isNavbarOpen) {
    public static DocumentAttribute of(DocumentContentData documentData, DocumentPaginationData paginationData, boolean isNavbarOpen) {
        return new DocumentAttribute(documentData, paginationData, isNavbarOpen);
    }
}
