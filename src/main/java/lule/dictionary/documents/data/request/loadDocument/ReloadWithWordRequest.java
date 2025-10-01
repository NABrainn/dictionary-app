package lule.dictionary.documents.data.request.loadDocument;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReloadWithWordRequest(int documentId,
                                    int page,
                                    boolean isUnitPersisted,
                                    @NonNull String unitText,
                                    int startId,
                                    int length) implements LoadDocumentRequest, ReloadRequest {
    @Override
    public int length() {
        return 1;
    }
}