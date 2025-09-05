package lule.dictionary.documents.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.data.entity.Document;

@Builder
public record AssembleDocumentAttributeRequest(int wordId,
                                               int documentId,
                                               int page,
                                               @NonNull Document document,
                                               int totalLength) {
}
