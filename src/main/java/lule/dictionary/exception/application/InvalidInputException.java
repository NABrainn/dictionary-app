package lule.dictionary.exception.application;

import jakarta.validation.ConstraintViolation;

public class InvalidInputException extends RuntimeException {

    private final ConstraintViolation<?> violation;

    public InvalidInputException(ConstraintViolation<?> violation) {
        this.violation = violation;
    }
}
