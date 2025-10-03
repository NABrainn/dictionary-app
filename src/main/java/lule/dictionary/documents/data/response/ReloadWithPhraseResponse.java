package lule.dictionary.documents.data.response;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.documents.data.request.loadDocument.SelectedPhraseInfo;
import lule.dictionary.pagination.data.DocumentPaginationData;

public record ReloadWithPhraseResponse(@NonNull DocumentContentData contentData,
                                       @NonNull DocumentPaginationData paginationData,
                                       @NonNull SelectedPhraseInfo selectedPhraseInfo) implements ReadDocumentResponse {
    public static ReloadWithPhraseResponse of(DocumentContentData contentData, DocumentPaginationData paginationData, SelectedPhraseInfo selectedPhraseInfo) {
        return new ReloadWithPhraseResponse(contentData, paginationData, selectedPhraseInfo);
    }
}
