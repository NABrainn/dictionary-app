package lule.dictionary.service.imports.importPageService.dto;

import lule.dictionary.service.translation.dto.TranslationModel;

public record SaveTranslationRequest(int wordId, int importId, TranslationModel translationModel) {
    @Override
    public TranslationModel translationModel() {
        if(translationModel == null) {
            throw new NullPointerException("Translation model is null");
        }
        return translationModel;
    }
}
