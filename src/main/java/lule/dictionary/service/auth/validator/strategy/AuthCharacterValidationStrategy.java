package lule.dictionary.service.auth.validator.strategy;

import lule.dictionary.component.validator.strategy.interfaces.CharacterValidationStrategy;
import lule.dictionary.service.auth.validator.exception.ValidationStrategyException;
import org.springframework.stereotype.Component;

@Component
public class AuthCharacterValidationStrategy implements CharacterValidationStrategy {
    @Override
    public String validate(String pattern, String field) {
        if(!field.matches(pattern)) throw new ValidationStrategyException("Field cannot contain illegal characters");
        return field;
    }
}
