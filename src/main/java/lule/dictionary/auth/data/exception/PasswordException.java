package lule.dictionary.auth.data.exception;

import jakarta.validation.ConstraintViolation;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.ValidationException;

public class PasswordException extends ValidationException {

    private final Language language;
    private final ConstraintViolation<?> violation;

    public PasswordException(Language language, ConstraintViolation<?> violation) {
        this.language = language;
        this.violation = violation;
    }

    @Override
    public String getViolationMessage() {
        return switch (violation.getMessage()) {
            case "PASSWORD_CANNOT_BE_EMPTY" -> switch (language) {
                case PL -> "Hasło nie może być puste.";
                case EN -> "Password cannot be empty.";
                case IT -> "La password non può essere vuota.";
                case NO -> "Passord kan ikke være tomt.";
            };
            case "PASSWORD_MUST_BE_BETWEEN_8_AND_200_CHARACTERS" -> switch (language) {
                case PL -> "Hasło musi mieć od 8 do 200 znaków.";
                case EN -> "Password must be between 8 and 200 characters.";
                case IT -> "La password deve avere tra 8 e 200 caratteri.";
                case NO -> "Passord må være mellom 8 og 200 tegn.";
            };
            default -> throw new IllegalStateException("Unexpected value: " + violation.getMessage());
        };
    }
}
