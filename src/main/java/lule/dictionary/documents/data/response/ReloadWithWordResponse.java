package lule.dictionary.documents.data.response;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.documents.data.documentProcessing.SelectedWordUnit;
import lule.dictionary.pagination.data.PaginationData;

public record ReloadWithWordResponse(@NonNull DocumentContentData contentData,
                                     @NonNull PaginationData paginationData,
                                     @NonNull SelectedWordUnit selectedWordInfo) implements ReadDocumentResponse {
    public static ReadDocumentResponse of(DocumentContentData contentData, PaginationData paginationData, SelectedWordUnit selectedWordInfo) {
        return new ReloadWithWordResponse(contentData, paginationData, selectedWordInfo);
    }
}
