package lule.dictionary.auth.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.auth.data.localization.AuthLocalizationKey;
import lule.dictionary.language.service.Language;
import org.springframework.stereotype.Service;

import java.util.Map;

import static lule.dictionary.auth.data.localization.AuthLocalizationKey.*;

@Service
@RequiredArgsConstructor
public class AuthLocalizationService {
    public Map<AuthLocalizationKey, String> getLocalization(Language language) {
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
}
