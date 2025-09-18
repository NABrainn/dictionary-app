package lule.dictionary.validation.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class Validator {
    public Result<?> validate(List<Constraint> constraints) {
        Stream<Constraint> violationStream = constraints.stream()
                .filter(constraint -> constraint.violationChecker().run())
                .distinct();

        Map<String, String> violations = violationStream
                .collect(Collectors.toUnmodifiableMap(Constraint::name, Constraint::message, (v1, v2) -> v1));

        return violations.isEmpty()
                ? Ok.empty()
                : Err.of(new ValidationException(violations));
    }
}
