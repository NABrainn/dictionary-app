package lule.dictionary.service.imports.importService.dto.importPageRequest;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import org.springframework.stereotype.Service;

@Service
public class ImportPageRequestFactory {

    public AssembleImportPageRequest of(int wordId,
                                        int importId,
                                        int page,
                                        @NonNull ImportWithPagination importWithPagination,
                                        int totalLength) {
        return new AssembleImportPageRequest(wordId, importId, page, importWithPagination, totalLength);
    }
}
