package lule.dictionary.util.errors;

import jakarta.validation.ConstraintViolation;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceResultFactory {

    public static <T> Map<String, String> fromSet(Set<ConstraintViolation<T>> result) {
        return result.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableMap(
                        ConstraintViolation::getMessage,
                        v -> v.getPropertyPath().toString()));
    }
}
