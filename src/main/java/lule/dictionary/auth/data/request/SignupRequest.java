package lule.dictionary.auth.data.request;

import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;
import lule.dictionary.validation.data.ValidationException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record SignupRequest(
        String login,
        String email,
        String password) implements AuthRequest, Validated {
    public static SignupRequest of(String login, String email, String password) {
        return new SignupRequest(login, email, password);
    }

    @Override
    public Set<Map<String, String>> validate(Language language) {
        Set<Map<String, String>> violations = new HashSet<>();
        if (login.isBlank()) {
            violations.add(Map.of(
                    "login",
                    switch (language) {
                        case PL -> "Nazwa użytkownika nie może być pusta";
                        case EN -> "Username cannot be empty";
                        case IT -> "Il nome utente non può essere vuoto";
                        case NO -> "Brukernavn kan ikke være tomt";
                    })
            );
        }
        if (login.length() < 8 || login.length() > 50) {
            throw new ValidationException(Map.of(
                    "login",
                    switch (language) {
                        case PL -> "Nazwa użytkownika musi mieć od 8 do 50 znaków";
                        case EN -> "Username must be between 8 and 50 characters";
                        case IT -> "Il nome utente deve avere tra 8 e 50 caratteri";
                        case NO -> "Brukernavn må være mellom 8 og 50 tegn";
                    })
            );
        }
        if (email.isBlank()) {
            violations.add(Map.of(
                    "email",
                    switch (language) {
                        case PL -> "Adres e-mail nie może być pusty";
                        case EN -> "Email address cannot be empty";
                        case IT -> "L'indirizzo email non può essere vuoto";
                        case NO -> "E-postadressen kan ikke være tom";
                    })
            );
        }
        if (email.length() < 8 || email.length() > 300) {
            throw new ValidationException(Map.of(
                    "email",
                    switch (language) {
                        case PL -> "Adres e-mail musi mieć od 8 do 300 znaków";
                        case EN -> "Email address must be between 8 and 300 characters";
                        case IT -> "L'indirizzo email deve avere tra 8 e 300 caratteri";
                        case NO -> "E-postadressen må være mellom 8 og 300 tegn";
                    })
            );
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException(Map.of(
                    "email",
                    switch (language) {
                        case PL -> "Nieprawidłowy format adresu e-mail";
                        case EN -> "Invalid email address format";
                        case IT -> "Formato dell'indirizzo email non valido";
                        case NO -> "Ugyldig format på e-postadressen";
                    })
            );
        }
        if (password.isBlank()) {
            violations.add(Map.of(
                    "password",
                    switch (language) {
                        case PL -> "Hasło nie może być puste";
                        case EN -> "Password cannot be empty";
                        case IT -> "La password non può essere vuota";
                        case NO -> "Passord kan ikke være tomt";
                    })
            );
        }
        if (password.length() < 8 || password.length() > 200) {
            throw new ValidationException(Map.of(
                    "password",
                    switch (language) {
                        case PL -> "Hasło musi mieć od 8 do 200 znaków";
                        case EN -> "Password must be between 8 and 200 characters";
                        case IT -> "La password deve avere tra 8 e 200 caratteri";
                        case NO -> "Passord må være mellom 8 og 200 tegn";
                    })
            );
        }
        return violations;
    }
}