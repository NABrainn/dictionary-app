package lule.dictionary.service.imports.importService.dto.importPageRequest;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;

public record AssembleImportPageRequest(int wordId,
                                        int importId,
                                        int page,
                                        @NonNull ImportWithPagination importWithPagination,
                                        int totalLength) {
}
