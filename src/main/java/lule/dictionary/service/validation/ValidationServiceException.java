package lule.dictionary.service.validation;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationServiceException extends RuntimeException {

    private final Map<String, String> errorMessages;

    public ValidationServiceException(String message, Map<String, String> errors) {
        super(message);
        this.errorMessages = errors;
    }
    public ValidationServiceException(Map<String, String> errors) {
        this.errorMessages = errors;
    }
}
