package lule.dictionary.service.imports.importService.dto.importsAttribute;

import lombok.NonNull;
import lule.dictionary.service.imports.importService.dto.importData.ImportData;
import lule.dictionary.service.pagination.dto.PaginationData;
import org.springframework.stereotype.Service;

@Service
public class ImportsAttributeFactory {

    public ImportsAttribute of(@NonNull ImportData importData,
                               @NonNull PaginationData paginationData) {
        return new ImportsAttribute(importData, paginationData);
    }
}
