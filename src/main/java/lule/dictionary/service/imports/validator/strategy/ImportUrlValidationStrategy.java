package lule.dictionary.service.imports.validator.strategy;

import lule.dictionary.component.validator.strategy.interfaces.UrlValidationStrategy;
import lule.dictionary.service.auth.validator.exception.ValidationStrategyException;
import org.springframework.stereotype.Component;

@Component
public class ImportUrlValidationStrategy implements UrlValidationStrategy {
    @Override
    public String validate(String field) {
        if (!field.matches("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*\\?$")) {
            throw new ValidationStrategyException("Invalid url provided");
        }
        return field;
    }
}
