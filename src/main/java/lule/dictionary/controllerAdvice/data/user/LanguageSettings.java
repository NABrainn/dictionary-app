package lule.dictionary.controllerAdvice.data.user;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record LanguageSettings(@NonNull Language uiLanguage,
                               @NonNull Language sourceLanguage,
                               @NonNull Language targetLanguage) {
    public static LanguageSettings of(Language uiLanguage,
                                   Language sourceLanguage,
                                   Language targetLanguage) {
        return new LanguageSettings(uiLanguage, sourceLanguage, targetLanguage);
    }
}
