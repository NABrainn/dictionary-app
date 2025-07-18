package lule.dictionary.service.imports.importService.dto.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.ImportWithPagination;

@Builder
public record AssembleImportContentRequest(int wordId,
                                           int importId,
                                           int page,
                                           @NonNull ImportWithPagination importWithPagination,
                                           int totalLength) {
}
