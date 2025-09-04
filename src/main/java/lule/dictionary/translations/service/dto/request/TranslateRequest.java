package lule.dictionary.translations.service.dto.request;

import lule.dictionary.language.service.Language;

public record TranslateRequest(String targetWord, Language sourceLanguage, Language targetLanguage) {
    public static TranslateRequest of(String targetWord, Language sourceLanguage, Language targetLanguage) {
        return new TranslateRequest(targetWord, sourceLanguage, targetLanguage);
    }
}
