package lule.dictionary.service.imports.importService.dto.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.DocumentWithContent;

@Builder
public record AssembleDocumentContentRequest(int wordId,
                                             int importId,
                                             int page,
                                             @NonNull DocumentWithContent documentWithContent,
                                             int totalLength) {
}
