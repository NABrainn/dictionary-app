package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record ReloadWithPhrase(@NonNull DocumentInfo documentInfo,
                               @NonNull SelectedUnitCords selectedUnitInfo) implements ReloadDocumentRequest {
    public static ReloadWithPhrase of(DocumentInfo documentInfo, SelectedUnitCords selectedUnitInfo) {
        return new ReloadWithPhrase(documentInfo, selectedUnitInfo);
    }
}
