package lule.dictionary.controllerAdvice.data.user;

import lombok.NonNull;

public record AuthenticatedUser(@NonNull String _csrf,
                                @NonNull String username,
                                @NonNull LanguageSettings languageSettings,
                                @NonNull LanguageProgression languageProgression) implements User {
    public static AuthenticatedUser of(String _csrf,
                                       String username,
                                       LanguageSettings languageSettings,
                                       LanguageProgression languageProgression) {
        return new AuthenticatedUser(_csrf, username, languageSettings, languageProgression);
    }
}
