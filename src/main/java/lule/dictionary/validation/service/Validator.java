package lule.dictionary.validation.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Validator {
    public void validate(List<Constraint> constraints) throws ValidationException {
        Map<String, String> violations = constraints.stream()
                .filter(constraint -> constraint.validationRunner().run())
                .distinct()
                .collect(Collectors.toUnmodifiableMap(Constraint::name, Constraint::message));
        if(!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }

}
