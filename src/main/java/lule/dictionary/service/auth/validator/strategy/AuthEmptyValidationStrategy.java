package lule.dictionary.service.auth.validator.strategy;

import lule.dictionary.component.validator.strategy.interfaces.EmptyValidationStrategy;
import lule.dictionary.service.auth.validator.exception.ValidationStrategyException;
import org.springframework.stereotype.Component;

@Component
public class AuthEmptyValidationStrategy implements EmptyValidationStrategy {

    @Override
    public String validate(String field) {
        if(field.isEmpty()) throw new ValidationStrategyException("Field cannot be empty");
        return field;
    }
}
