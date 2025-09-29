package lule.dictionary.documents.data.request.loadDocument;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FirstLoadRequest(int documentId,
                               int page,
                               int unitId,
                               boolean isUnitPersisted,
                               @NonNull String unitText) implements LoadDocumentRequest {
}
