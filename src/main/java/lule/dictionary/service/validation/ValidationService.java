package lule.dictionary.service.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public interface ValidationService {
    <T> T validate(T ob) throws ConstraintViolationException;
}
