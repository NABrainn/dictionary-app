package lule.dictionary.documents.data.request;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AssembleDocumentContentData(@NonNull String contentBlob,
                                          @NonNull String owner,
                                          @NonNull String title,
                                          int selectableId,
                                          int documentId) {
}
