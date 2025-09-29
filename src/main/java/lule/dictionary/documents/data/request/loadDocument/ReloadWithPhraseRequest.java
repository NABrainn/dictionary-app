package lule.dictionary.documents.data.request.loadDocument;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReloadWithPhraseRequest(int documentId,
                                      int page,
                                      int startId,
                                      int endId,
                                      boolean isUnitPersisted,
                                      @NonNull String unitText) implements LoadDocumentRequest, ReloadRequest {
}
