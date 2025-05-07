package lule.dictionary.entity.validation;

@FunctionalInterface
public interface EmptyValidator {
    void validate(String... fields);
}
