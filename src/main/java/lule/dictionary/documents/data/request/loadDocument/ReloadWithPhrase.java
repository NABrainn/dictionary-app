package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record ReloadWithPhrase(@NonNull DocumentInfo documentInfo,
                               @NonNull SelectedPhraseInfo selectedUnitInfo) implements ReloadDocumentRequest {
    public static ReloadWithPhrase of(DocumentInfo documentInfo, SelectedPhraseInfo selectedUnitInfo) {
        return new ReloadWithPhrase(documentInfo, selectedUnitInfo);
    }
}
