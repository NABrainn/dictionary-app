package lule.dictionary.functionalInterface;

@FunctionalInterface
public interface LengthValidator {
    void validate(int length, String field);
}
