package lule.dictionary.service.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationServiceImp implements ValidationService {

    private final Validator validator;

    public <T> Set<ConstraintViolation<T>> getConstraintViolations(T ob) {
        return validator.validate(ob);
    }

    public <T> void checkIfViolated(Set<ConstraintViolation<T>> constraints) {
        if(!constraints.isEmpty()) {
            throw new ConstraintViolationException(constraints);
        }
    }

    @Override
    public <T> T validate(T ob) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> constraintViolations = getConstraintViolations(ob);
        checkIfViolated(constraintViolations);
        return ob;
    }
}
