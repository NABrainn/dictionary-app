package lule.dictionary.util.errors;

import jakarta.validation.ConstraintViolation;

import java.util.*;
import java.util.stream.Collectors;


public class ErrorMapFactory {

    public static <T> Map<String, String> fromSetTyped(Set<ConstraintViolation<T>> result) {
        Map<String, String> sorted = result.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, ignored) -> existing,
                        TreeMap::new
                ));
        return Collections.unmodifiableMap(sorted);
    }

    public static <T> Map<String, String> fromSetWildcard(Set<ConstraintViolation<?>> result) {
        Map<String, String> sorted = result.stream()
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
