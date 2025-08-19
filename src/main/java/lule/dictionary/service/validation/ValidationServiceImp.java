package lule.dictionary.service.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationServiceImp implements ValidationService {

    private final Validator validator;

    @Override
    public <T> void validate(T ob) throws ValidationServiceException {
        Optional<ConstraintViolation<T>> violation = getFirstViolation(ob);
        if(violation.isPresent()) {
            throw new ValidationServiceException(violation.get().getPropertyPath() + " " + violation.get().getMessage(), violation.get());
        }
    }

    private <T> Optional<ConstraintViolation<T>> getFirstViolation(T ob) {
        return validator.validate(ob).stream()
                .sorted(Comparator.comparing(violation -> violation.getMessage().length()))
                .findFirst();
    }
}
