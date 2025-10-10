package lule.dictionary.controllerAdvice.data.user;

public sealed interface User permits AnonymousUser, AuthenticatedUser{
    String _csrf();
    LanguageSettings languageSettings();
}
