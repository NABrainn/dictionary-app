package lule.dictionary.service.validation;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationServiceException extends RuntimeException {

    private final ConstraintViolation<?> violation;

    public ValidationServiceException(String message, ConstraintViolation<?> violation) {
        super(message);
        this.violation = violation;
    }

    public ValidationServiceException(ConstraintViolation<?> violation) {
        this.violation = violation;
    }
}
