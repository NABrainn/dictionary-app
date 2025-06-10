package lule.dictionary.service.auth.validator;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.auth.validator.exception.ValidationException;
import lule.dictionary.service.auth.validator.exception.ValidationStrategyException;
import lule.dictionary.service.auth.validator.strategy.AuthEmptyValidationStrategy;
import lule.dictionary.service.auth.validator.strategy.AuthMaxLengthValidationStrategy;
import lule.dictionary.service.auth.validator.strategy.AuthMinLengthValidationStrategy;
import lule.dictionary.service.auth.validator.strategy.AuthCharacterValidationStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final AuthEmptyValidationStrategy emptyValidationStrategy;
    private final AuthMinLengthValidationStrategy minLengthValidationStrategy;
    private final AuthMaxLengthValidationStrategy maxLengthValidationStrategy;
    private final AuthCharacterValidationStrategy characterValidationStrategy;


    public String validateUsername(String field) throws ValidationStrategyException {
        try {
            emptyValidationStrategy.validate(field);
            minLengthValidationStrategy.validate(8, field);
            maxLengthValidationStrategy.validate(30, field);
            characterValidationStrategy.validate("^(?!.*[!@#$%^&*()]).*$", field);
            return field;
        }
        catch (ValidationStrategyException e) {
            throw new ValidationException("Username error: " + e.getMessage());
        }
    }
    public String validateEmail(String field) throws ValidationStrategyException {
        try {
            emptyValidationStrategy.validate(field);
            minLengthValidationStrategy.validate(8, field);
            maxLengthValidationStrategy.validate(30, field);
            characterValidationStrategy.validate("^[a-zA-Z0-9._%+-@]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", field);
            return field;
        }
        catch (ValidationStrategyException e) {
            throw new ValidationException("Email error: " + e.getMessage());
        }
    }
    public String validatePassword(String field) throws ValidationStrategyException {
        try {
            emptyValidationStrategy.validate(field);
            minLengthValidationStrategy.validate(8, field);
            return field;
        }
        catch (ValidationStrategyException e) {
            throw new ValidationException("Password error: " + e.getMessage());
        }
    }
}
