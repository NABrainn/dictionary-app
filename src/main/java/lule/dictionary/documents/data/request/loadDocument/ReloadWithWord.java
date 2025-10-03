package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record ReloadWithWord(@NonNull DocumentInfo documentInfo,
                             @NonNull SelectedWordInfo selectedUnitInfo) implements ReloadDocumentRequest {
    public static ReloadWithWord of(DocumentInfo documentInfo, SelectedWordInfo selectedUnitInfo) {
        return new ReloadWithWord(documentInfo, selectedUnitInfo);
    }
}
