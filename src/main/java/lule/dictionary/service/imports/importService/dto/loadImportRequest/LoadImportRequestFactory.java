package lule.dictionary.service.imports.importService.dto.loadImportRequest;

import org.springframework.stereotype.Service;

@Service
public class LoadImportRequestFactory {

    public LoadImportRequest of(int importId,
                                int page) {
        return new LoadImportRequest(importId, page);
    }
}
