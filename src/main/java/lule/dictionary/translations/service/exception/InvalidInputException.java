package lule.dictionary.translations.service.exception;

import jakarta.validation.ConstraintViolation;

public class InvalidInputException extends RuntimeException {

    private final ConstraintViolation<?> violation;

    public InvalidInputException(ConstraintViolation<?> violation) {
        this.violation = violation;
    }
}
