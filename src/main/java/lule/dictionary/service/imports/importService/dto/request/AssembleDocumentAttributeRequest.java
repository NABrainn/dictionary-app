package lule.dictionary.service.imports.importService.dto.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.base.Document;

@Builder
public record AssembleDocumentAttributeRequest(int wordId,
                                               int documentId,
                                               int page,
                                               @NonNull Document document,
                                               int totalLength) {
}
