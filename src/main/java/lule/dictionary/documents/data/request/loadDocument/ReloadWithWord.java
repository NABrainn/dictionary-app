package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record ReloadWithWord(@NonNull DocumentDetails documentDetails,
                             @NonNull SelectedUnitDetails selectedUnitInfo) implements ReloadDocumentRequest {
    public static ReloadWithWord of(DocumentDetails documentDetails, SelectedUnitDetails selectedUnitInfo) {
        return new ReloadWithWord(documentDetails, selectedUnitInfo);
    }
}
