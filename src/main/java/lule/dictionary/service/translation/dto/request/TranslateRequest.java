package lule.dictionary.service.translation.dto.request;

import lule.dictionary.service.language.Language;

public record TranslateRequest(String targetWord, Language sourceLanguage, Language targetLanguage) {
    public static TranslateRequest of(String targetWord, Language sourceLanguage, Language targetLanguage) {
        return new TranslateRequest(targetWord, sourceLanguage, targetLanguage);
    }
}
