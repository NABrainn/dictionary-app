package lule.dictionary.translations.data.request;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record ConfigureFlashcardRequest(int familiarity,
                                        int quantity,
                                        boolean isPhrase,
                                        @NonNull Language systemLanguage) {
    public static ConfigureFlashcardRequest of(int familiarity, int quantity, boolean isPhrase, Language systemLanguage) {
        return new ConfigureFlashcardRequest(familiarity, quantity, isPhrase, systemLanguage);
    }
}
