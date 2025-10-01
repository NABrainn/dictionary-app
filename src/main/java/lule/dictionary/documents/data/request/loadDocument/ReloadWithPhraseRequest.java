package lule.dictionary.documents.data.request.loadDocument;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReloadWithPhraseRequest(int documentId,
                                      int page,
                                      boolean isUnitPersisted,
                                      @NonNull String unitText,
                                      int startId,
                                      int length) implements LoadDocumentRequest, ReloadRequest {
}
