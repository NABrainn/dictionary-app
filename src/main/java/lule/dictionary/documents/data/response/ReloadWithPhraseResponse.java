package lule.dictionary.documents.data.response;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.documents.data.documentProcessing.SelectedPhraseUnit;
import lule.dictionary.pagination.data.PaginationData;

public record ReloadWithPhraseResponse(@NonNull DocumentContentData contentData,
                                       @NonNull PaginationData paginationData,
                                       @NonNull SelectedPhraseUnit selectedPhraseInfo) implements ReadDocumentResponse {
    public static ReadDocumentResponse of(DocumentContentData contentData, PaginationData paginationData, SelectedPhraseUnit selectedPhraseInfo) {
        return new ReloadWithPhraseResponse(contentData, paginationData, selectedPhraseInfo);
    }
}
