package lule.dictionary.validation.data;

public abstract class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }

  protected ValidationException() {
  }

    public abstract String getViolationMessage();
}
