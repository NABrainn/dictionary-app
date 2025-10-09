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
    public Map<AuthError, String> errorLocalization(Language language) {
        return switch (language) {
            case PL -> Map.of(
                    AuthError.EMPTY_LOGIN, "Nazwa użytkownika nie może być pusta",
                    AuthError.SIZE_LOGIN, "Nazwa użytkownika nie może być dłuższa niż 50 znaków",
                    AuthError.EMPTY_PASSWORD, "Hasło nie może być puste",
                    AuthError.SIZE_PASSWORD, "Hasło nie może być dłuższe niż 500 znaków",
                    AuthError.EMPTY_EMAIL, "Adres e-mail nie może być pusty",
                    AuthError.INVALID_EMAIL, "Nieprawidłowy format adresu e-mail",
                    AuthError.SIZE_EMAIL, "Adres e-mail nie może być dłuższy niż 200 znaków",
                    AuthError.USER_NOT_FOUND, "Użytkownik nie został znaleziony",
                    AuthError.USER_EXISTS, "Użytkownik już istnieje"
            );
            case EN -> Map.of(
                    AuthError.EMPTY_LOGIN, "Username cannot be empty",
                    AuthError.SIZE_LOGIN, "Username cannot be longer than 50 characters",
                    AuthError.EMPTY_PASSWORD, "Password cannot be empty",
                    AuthError.SIZE_PASSWORD, "Password cannot be longer than 500 characters",
                    AuthError.EMPTY_EMAIL, "Email address cannot be empty",
                    AuthError.INVALID_EMAIL, "Invalid email address format",
                    AuthError.SIZE_EMAIL, "Email address cannot be longer than 200 characters",
                    AuthError.USER_NOT_FOUND, "User not found",
                    AuthError.USER_EXISTS, "User already exists"
            );
            case IT -> Map.of(
                    AuthError.EMPTY_LOGIN, "Il nome utente non può essere vuoto",
                    AuthError.SIZE_LOGIN, "Il nome utente non può essere più lungo di 50 caratteri",
                    AuthError.EMPTY_PASSWORD, "La password non può essere vuota",
                    AuthError.SIZE_PASSWORD, "La password non può essere più lunga di 500 caratteri",
                    AuthError.EMPTY_EMAIL, "L'indirizzo email non può essere vuoto",
                    AuthError.INVALID_EMAIL, "Formato dell'indirizzo email non valido",
                    AuthError.SIZE_EMAIL, "L'indirizzo email non può essere più lungo di 200 caratteri",
                    AuthError.USER_NOT_FOUND, "Utente non trovato",
                    AuthError.USER_EXISTS, "L'utente esiste già"
            );
            case NO -> Map.of(
                    AuthError.EMPTY_LOGIN, "Brukernavnet kan ikke være tomt",
                    AuthError.SIZE_LOGIN, "Brukernavnet kan ikke være lenger enn 50 tegn",
                    AuthError.EMPTY_PASSWORD, "Passordet kan ikke være tomt",
                    AuthError.SIZE_PASSWORD, "Passordet kan ikke være lenger enn 500 tegn",
                    AuthError.EMPTY_EMAIL, "E-postadressen kan ikke være tom",
                    AuthError.INVALID_EMAIL, "Ugyldig format for e-postadresse",
                    AuthError.SIZE_EMAIL, "E-postadressen kan ikke være lenger enn 200 tegn",
                    AuthError.USER_NOT_FOUND, "Bruker ikke funnet",
                    AuthError.USER_EXISTS, "Brukeren finnes allerede"
            );
        };
    }
}
