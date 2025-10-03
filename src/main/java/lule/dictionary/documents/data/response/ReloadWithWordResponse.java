package lule.dictionary.documents.data.response;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.documents.data.documentProcessing.SelectedWordUnit;
import lule.dictionary.pagination.data.DocumentPaginationData;

public record ReloadWithWordResponse(@NonNull DocumentContentData contentData,
                                     @NonNull DocumentPaginationData paginationData,
                                     @NonNull SelectedWordUnit selectedWordInfo) implements ReadDocumentResponse {
    public static ReadDocumentResponse of(DocumentContentData contentData, DocumentPaginationData paginationData, SelectedWordUnit selectedWordInfo) {
        return new ReloadWithWordResponse(contentData, paginationData, selectedWordInfo);
    }
}
