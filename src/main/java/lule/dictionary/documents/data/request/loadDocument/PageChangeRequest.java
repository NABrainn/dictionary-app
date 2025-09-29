package lule.dictionary.documents.data.request.loadDocument;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record PageChangeRequest(int documentId,
                                int page,
                                int unitId,
                                boolean isUnitPersisted,
                                @NonNull String unitText) implements LoadDocumentRequest {
}
