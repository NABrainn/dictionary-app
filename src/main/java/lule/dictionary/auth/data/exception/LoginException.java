package lule.dictionary.auth.data.exception;

import jakarta.validation.ConstraintViolation;
import lombok.NonNull;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.ValidationException;

public class LoginException extends ValidationException {

    private final Language language;
    private final ConstraintViolation<?> violation;

    public LoginException(@NonNull Language language, ConstraintViolation<?> violation) {
        this.language = language;
        this.violation = violation;
    }

    @Override
    public String getViolationMessage() {
        return switch (violation.getMessage()) {
            case "LOGIN_CANNOT_BE_EMPTY" -> switch (language) {
                case PL -> "Login nie może być pusty.";
                case EN -> "Login cannot be empty.";
                case IT -> "Il login non può essere vuoto.";
                case NO -> "Innlogging kan ikke være tom.";
            };
            case "LOGIN_MUST_CONTAIN_ONLY_LETTERS" -> switch (language) {
                case PL -> "Login może zawierać tylko litery.";
                case EN -> "Login must contain only letters.";
                case IT -> "Il login deve contenere solo lettere.";
                case NO -> "Innlogging må kun inneholde bokstaver.";
            };
            case "LOGIN_MUST_BE_BETWEEN_8_AND_50_CHARACTERS" -> switch (language) {
                case PL -> "Login musi mieć od 8 do 50 znaków.";
                case EN -> "Login must be between 8 and 50 characters.";
                case IT -> "Il login deve avere tra 8 e 50 caratteri.";
                case NO -> "Innlogging må være mellom 8 og 50 tegn.";
            };
            default -> throw new IllegalStateException("Unexpected value: " + violation.getMessage());
        };
    }
}
