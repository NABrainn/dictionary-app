package lule.dictionary.documents.data.response;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.DocumentPaginationData;


public record LoadDocumentResponse(@NonNull DocumentContentData contentData,
                                   @NonNull DocumentPaginationData paginationData,
                                   boolean isNavbarOpen) implements ReadDocumentResponse {
    public static LoadDocumentResponse of(DocumentContentData contentData,
                                          DocumentPaginationData paginationData,
                                          boolean isNavbarOpen) {
        return new LoadDocumentResponse(contentData, paginationData, isNavbarOpen);
    }
}
