package lule.dictionary.documents.data.response;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.PaginationData;


public record LoadDocumentResponse(@NonNull DocumentContentData contentData,
                                   @NonNull PaginationData paginationData) implements ReadDocumentResponse {
    public static LoadDocumentResponse of(DocumentContentData contentData,
                                          PaginationData paginationData) {
        return new LoadDocumentResponse(contentData, paginationData);
    }
}
