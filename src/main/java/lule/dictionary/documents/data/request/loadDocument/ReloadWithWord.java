package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record ReloadWithWord(@NonNull DocumentInfo documentInfo,
                             @NonNull SelectedUnitCords selectedUnitInfo) implements ReloadDocumentRequest {
    public static ReloadWithWord of(DocumentInfo documentInfo, SelectedUnitCords selectedUnitInfo) {
        return new ReloadWithWord(documentInfo, selectedUnitInfo);
    }
}
