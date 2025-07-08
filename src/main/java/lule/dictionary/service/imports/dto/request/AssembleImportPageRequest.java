package lule.dictionary.service.imports.dto.request;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import lule.dictionary.service.dto.request.ServiceRequest;

public record AssembleImportPageRequest(int wordId,
                                        int importId,
                                        int page,
                                        @NonNull ImportWithPagination importWithPagination,
                                        int totalLength) implements ServiceRequest {
}
