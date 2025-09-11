package lule.dictionary.validation.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;

    public <T> Optional<ConstraintViolation<T>> validate(T ob, Class<?> group) {
        return validator.validate(ob, group).stream()
                .findFirst();
    }
}
