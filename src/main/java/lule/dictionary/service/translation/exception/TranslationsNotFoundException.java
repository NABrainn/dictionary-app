package lule.dictionary.service.translation.exception;

public class TranslationsNotFoundException extends RuntimeException {
    public TranslationsNotFoundException(String message) {
        super(message);
    }
}
