package lule.dictionary.translations.data.entity;

public sealed interface UninitializedTranslation
        extends Translation
        permits UninitializedWordTranslation, UninitializedPhrase {
}
