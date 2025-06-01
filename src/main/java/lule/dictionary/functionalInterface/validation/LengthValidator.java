package lule.dictionary.functionalInterface.validation;

@FunctionalInterface
public interface LengthValidator {
    void validate(int length, String field);
}
