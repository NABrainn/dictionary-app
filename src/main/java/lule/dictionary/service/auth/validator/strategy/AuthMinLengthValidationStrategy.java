package lule.dictionary.service.auth.validator.strategy;

import lule.dictionary.component.validator.strategy.interfaces.MinLengthValidationStrategy;
import lule.dictionary.service.auth.validator.exception.ValidationStrategyException;
import org.springframework.stereotype.Component;

@Component
public class AuthMinLengthValidationStrategy implements MinLengthValidationStrategy {
    @Override
    public String validate(int minLength, String field) {
        if (field.length() < minLength) throw new ValidationStrategyException("Field must be longer than " + minLength + " characters");
        return field;
    }
}
