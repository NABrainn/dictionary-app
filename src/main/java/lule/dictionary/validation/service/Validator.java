package lule.dictionary.validation.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Validator {
    public Result<?> validate(Constraint... constraints) {
        Map<String, String> violations = Arrays.stream(constraints)
                .filter(Predicate.not(constraint -> constraint.rule().isValid()))
                .distinct()
                .collect(Collectors.toUnmodifiableMap(Constraint::name, Constraint::message, (v1, v2) -> v1));
        System.out.println(violations);
        return violations.isEmpty()
                ? Ok.empty()
                : Err.of(new ValidationException(violations));
    }
}
