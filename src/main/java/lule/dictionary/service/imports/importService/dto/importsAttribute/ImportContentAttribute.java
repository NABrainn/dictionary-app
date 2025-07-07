package lule.dictionary.service.imports.importService.dto.importsAttribute;

import lombok.NonNull;
import lule.dictionary.service.imports.importService.dto.importData.ImportData;
import lule.dictionary.service.pagination.dto.PaginationData;

public record ImportContentAttribute(@NonNull ImportData importData,
                                     @NonNull PaginationData paginationData) {
}
