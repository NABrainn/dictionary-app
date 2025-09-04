package lule.dictionary.validation.service;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

@Getter
public class ValidationServiceException extends RuntimeException {

    private final ConstraintViolation<?> violation;

    public ValidationServiceException(String message, ConstraintViolation<?> violation) {
        super(message);
        this.violation = violation;
    }
}
