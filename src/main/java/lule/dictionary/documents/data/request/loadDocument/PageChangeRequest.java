package lule.dictionary.documents.data.request.loadDocument;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record PageChangeRequest(int documentId,
                                int page,
                                boolean isUnitPersisted,
                                @NonNull String unitText,
                                int startId,
                                int length) implements LoadDocumentRequest {
    @Override
    public int startId() {
        return -1;
    }
    @Override
    public int length() {
        return -1;
    }
}
