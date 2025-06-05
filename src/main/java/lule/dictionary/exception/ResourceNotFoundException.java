package lule.dictionary.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}