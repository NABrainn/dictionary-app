package lule.dictionary.functionalInterface;

@FunctionalInterface
public interface EmptyValidator {
    void validate(String... fields);
}
