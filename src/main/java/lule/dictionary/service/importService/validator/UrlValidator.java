package lule.dictionary.service.importService.validator;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.auth.validator.exception.ValidationException;
import lule.dictionary.service.auth.validator.exception.ValidationStrategyException;
import lule.dictionary.service.importService.validator.strategy.ImportUrlValidationStrategy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlValidator {

    private final ImportUrlValidationStrategy urlValidationStrategy;

    public void validate(String url) throws ValidationStrategyException {
        try {
            urlValidationStrategy.validate(url);
        } catch (ValidationStrategyException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
