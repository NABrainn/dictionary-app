package lule.dictionary.dto.application.implementation.userProfile;

import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.enumeration.Language;
import lule.dictionary.functionalInterface.validation.EqualEnumValueValidator;

public record DictionaryUserProfileSettings(
        Language sourceLanguage,
        Language targetLanguage) implements UserProfileSettings {
    public DictionaryUserProfileSettings {
        EqualEnumValueValidator<Language> equalValueValidator = (Language lang1, Language lang2) -> {
            if(lang1.equals(lang2)) throw new IllegalArgumentException("source language and target language cannot be equal");
        };

        equalValueValidator.validate(sourceLanguage, targetLanguage);
    }
}
