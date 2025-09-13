package lule.dictionary.auth.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.auth.data.localization.*;
import lule.dictionary.language.service.Language;
import org.springframework.stereotype.Service;

import java.util.Map;

import static lule.dictionary.auth.data.localization.AuthText.*;

@Service
@RequiredArgsConstructor
public class AuthLocalizationService {
    public Map<AuthText, String> getTextLocalization(Language language) {
        return switch (language) {
            case PL -> Map.of(
                    LOG_IN, "Zaloguj się",
                    SIGN_UP, "Zarejestruj się",
                    USERNAME, "Nazwa użytkownika",
                    EMAIL, "E-mail",
                    PASSWORD, "Hasło",
                    CREATE_ACCOUNT, "Utwórz konto",
                    SUBMIT, "Wyślij"
            );
            case EN -> Map.of(
                    LOG_IN, "Log in",
                    SIGN_UP, "Sign up",
                    USERNAME, "Username",
                    EMAIL, "Email",
                    PASSWORD, "Password",
                    CREATE_ACCOUNT, "Create account",
                    SUBMIT, "Submit"
            );
            case IT -> Map.of(
                    LOG_IN, "Accedi",
                    SIGN_UP, "Registrati",
                    USERNAME, "Nome utente",
                    EMAIL, "Email",
                    PASSWORD, "Password",
                    CREATE_ACCOUNT, "Crea account",
                    SUBMIT, "Invia"
            );
            case NO -> Map.of(
                    LOG_IN, "Logg inn",
                    SIGN_UP, "Registrer deg",
                    USERNAME, "Brukernavn",
                    EMAIL, "E-post",
                    PASSWORD, "Passord",
                    CREATE_ACCOUNT, "Opprett konto",
                    SUBMIT, "Send"
            );
        };
    }

    public Map<AuthError, String> getErrorLocalization(Language language) {
        return switch (language) {
            case PL -> Map.of(AuthError.USER_NOT_FOUND, "Użytkownik nie został znaleziony");
            case EN -> Map.of(AuthError.USER_NOT_FOUND, "User not found");
            case IT -> Map.of(AuthError.USER_NOT_FOUND, "Utente non trovato");
            case NO -> Map.of(AuthError.USER_NOT_FOUND, "Bruker ikke funnet");
        };
    }
}
