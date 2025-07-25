package lule.dictionary.dto.application;

import lule.dictionary.service.language.Language;

public record LanguageData(Language language, String fullName, String libreTranslateAbbreviation, String imgPath) {
    public static LanguageData of(Language language, String fullName, String libreTranslateAbbreviation, String imgPath) {
        return new LanguageData(language, fullName, libreTranslateAbbreviation, imgPath);
    }
}
