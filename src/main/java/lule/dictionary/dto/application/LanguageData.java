package lule.dictionary.dto.application;

import lule.dictionary.service.language.Language;

public record LanguageData(Language language, String fullName, String languageCode, String imgPath) {
    public static LanguageData of(Language language, String fullName, String languageCode, String imgPath) {
        return new LanguageData(language, fullName, languageCode, imgPath);
    }
}
