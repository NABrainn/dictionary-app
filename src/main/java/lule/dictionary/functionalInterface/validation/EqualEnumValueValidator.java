package lule.dictionary.functionalInterface.validation;

@FunctionalInterface
public interface EqualEnumValueValidator<T extends Enum<T>> {
    void validate(T enum1, T enum2);
}
