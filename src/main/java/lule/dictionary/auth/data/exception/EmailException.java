package lule.dictionary.auth.data.exception;

import jakarta.validation.ConstraintViolation;
import lombok.NonNull;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.ValidationException;

public class EmailException extends ValidationException {

    private final Language language;
    private final ConstraintViolation<?> violation;

    public EmailException(@NonNull Language language, ConstraintViolation<?> violation) {
        this.language = language;
        this.violation = violation;
    }

    @Override
    public String getViolationMessage() {
        return switch (violation.getMessage()) {
            case "EMAIL_CANNOT_BE_EMPTY" -> switch (language) {
                case PL -> "E-mail nie może być pusty.";
                case EN -> "Email cannot be empty.";
                case IT -> "L'email non può essere vuota.";
                case NO -> "E-post kan ikke være tom.";
            };
            case "EMAIL_MUST_BE_VALID" -> switch (language) {
                case PL -> "E-mail musi być prawidłowy.";
                case EN -> "Email must be valid.";
                case IT -> "L'email deve essere valida.";
                case NO -> "E-post må være gyldig.";
            };
            default -> throw new IllegalStateException("Unexpected value: " + violation.getMessage());
        };
    }
}
