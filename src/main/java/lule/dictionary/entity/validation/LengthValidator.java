package lule.dictionary.entity.validation;

@FunctionalInterface
public interface LengthValidator {
    void validate(int length, String field);
}
