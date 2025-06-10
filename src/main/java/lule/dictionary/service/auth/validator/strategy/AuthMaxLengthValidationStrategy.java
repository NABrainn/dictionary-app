package lule.dictionary.service.auth.validator.strategy;

import lule.dictionary.component.validator.strategy.interfaces.MaxLengthValidationStrategy;
import lule.dictionary.service.auth.validator.exception.ValidationStrategyException;
import org.springframework.stereotype.Component;

@Component
public class AuthMaxLengthValidationStrategy implements MaxLengthValidationStrategy {
    @Override
    public String validate(int maxLength, String field) {
        if(field.length() > maxLength) throw new ValidationStrategyException("Field cannot be longer than " + maxLength + " characters");
        return field;
    }
}
