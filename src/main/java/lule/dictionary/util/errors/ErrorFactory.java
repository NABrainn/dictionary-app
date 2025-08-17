package lule.dictionary.util.errors;

import jakarta.validation.ConstraintViolation;

import java.util.*;

public class ErrorFactory {

    public static Map<String, String> fromViolation(ConstraintViolation<?> violation) {
        return Map.of(violation.getPropertyPath().toString(), violation.getMessage());
    }
}
