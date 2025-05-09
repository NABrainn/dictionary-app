package lule.dictionary.validation;

@FunctionalInterface
public interface EmptyValidator {
    void validate(String... fields);
}
