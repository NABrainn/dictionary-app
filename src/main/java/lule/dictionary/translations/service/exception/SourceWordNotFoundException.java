package lule.dictionary.translations.service.exception;

public class SourceWordNotFoundException extends RuntimeException {
    public SourceWordNotFoundException(String message) {
        super(message);
    }
}
