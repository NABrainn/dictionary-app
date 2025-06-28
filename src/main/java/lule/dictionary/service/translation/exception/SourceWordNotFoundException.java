package lule.dictionary.service.translation.exception;

public class SourceWordNotFoundException extends RuntimeException {
    public SourceWordNotFoundException(String message) {
        super(message);
    }
}
