package lule.dictionary.service.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class ValidationServiceImp implements ValidationService {

    private final Validator validator;

    @Override
    public <T> void validate(T ob) throws ValidationServiceException {
        validator.validate(ob).stream()
                .sorted(Comparator.comparingInt(violation -> violation.getMessage().length()))
                .collect(groupingBy(ConstraintViolation::getPropertyPath))
                .values().stream()
                .filter(group -> !group.isEmpty())
                .findFirst()
                .orElseGet(List::of).stream()
                .findFirst()
                .ifPresent(violation -> {
                    throw new ValidationServiceException( violation.getPropertyPath() + " " + violation.getMessage(), violation);
                });
    }
}
