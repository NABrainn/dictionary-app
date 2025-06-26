package lule.dictionary.service.imports.importPageService.dto;

import lule.dictionary.service.translation.dto.TranslationModel;

public record LoadImportRequest(int wordId, int importId, int page, TranslationModel translationModel) {
    @Override
    public TranslationModel translationModel() {
        if(translationModel == null) {
            throw new NullPointerException("Translation model is null");
        }
        return translationModel;
    }
}
