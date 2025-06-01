package lule.dictionary.functionalInterface.validation;

@FunctionalInterface
public interface EmptyValidator {
    void validate(String... fields);
}
