package lule.dictionary.validation.data.rule;

public sealed interface Rule permits Email, NotEmpty, Size {
    boolean isValid();
}
