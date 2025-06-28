package lule.dictionary.service.imports.exception;

public class ImportNotFoundException extends RuntimeException {
    public ImportNotFoundException(String message) {
        super(message);
    }
}
