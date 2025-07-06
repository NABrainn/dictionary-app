package lule.dictionary.service.imports.importService.dto.loadImportPageRequest;

import org.springframework.stereotype.Service;

@Service
public class LoadImportPageRequestFactory {

    public LoadImportPageRequest of(int wordId, int importId, int page) {
        return new LoadImportPageRequest(wordId, importId, page);
    }
}
