package lule.dictionary.util.errors;

import jakarta.validation.ConstraintViolation;

import java.util.*;
import java.util.stream.Collectors;


public class ErrorMapFactory {

    public static Map<String, String> fromViolations(Set<? extends ConstraintViolation<?>> violations) {
        return toSortedErrorMap(violations);
    }


    private static Map<String, String> toSortedErrorMap(Set<? extends ConstraintViolation<?>> violations) {
        Map<String, String> sorted = violations.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, ignored) -> existing,
                        TreeMap::new
                ));
        return Collections.unmodifiableMap(sorted);
    }
}
