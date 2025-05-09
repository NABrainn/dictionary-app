package lule.dictionary.validation;

@FunctionalInterface
public interface LengthValidator {
    void validate(int length, String field);
}
