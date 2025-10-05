package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record ReloadWithPhrase(@NonNull DocumentDetails documentDetails,
                               @NonNull SelectedUnitDetails selectedUnitInfo) implements ReloadDocumentRequest {
    public static ReloadWithPhrase of(DocumentDetails documentDetails, SelectedUnitDetails selectedUnitInfo) {
        return new ReloadWithPhrase(documentDetails, selectedUnitInfo);
    }
}
