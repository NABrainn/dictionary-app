package lule.dictionary.documents.service.dto.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.data.Document;

@Builder
public record AssembleDocumentAttributeRequest(int wordId,
                                               int documentId,
                                               int page,
                                               @NonNull Document document,
                                               int totalLength) {
}
