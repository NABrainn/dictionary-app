package lule.dictionary.controllerAdvice.data.user;

import lombok.NonNull;

public record AnonymousUser(@NonNull String _csrf,
                            @NonNull LanguageSettings languageSettings) implements User {
    public static AnonymousUser of(String _csrf,
                                   LanguageSettings languageSettings) {
        return new AnonymousUser(_csrf, languageSettings);
    }
}
