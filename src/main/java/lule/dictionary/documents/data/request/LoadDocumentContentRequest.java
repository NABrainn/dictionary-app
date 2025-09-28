package lule.dictionary.documents.data.request;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record LoadDocumentContentRequest(@NonNull String type,
                                         int documentId,
                                         int page,
                                         int wordId,
                                         @NonNull String selectedTargetWord,
                                         boolean isSelectedPersisted) {
}
