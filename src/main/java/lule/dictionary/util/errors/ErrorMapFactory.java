package lule.dictionary.util.errors;

import jakarta.validation.ConstraintViolation;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ErrorMapFactory {

    public static <T> Map<String, String> fromSet(Set<ConstraintViolation<T>> result) {
        return result.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableMap(
                        k -> k.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing));
    }
}
