package lule.dictionary.dto.application.implementation.translation;

import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.translation.TranslationDetails;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.functionalInterface.validation.EmptyValidator;
import lule.dictionary.functionalInterface.validation.EqualEnumValueValidator;
import lule.dictionary.functionalInterface.validation.LengthValidator;
import lule.dictionary.functionalInterface.validation.PatternValidator;

import java.util.Arrays;
import java.util.regex.Pattern;

public record DictionaryTranslationDetails(
        @NonNull
        String sourceWord,
        @NonNull
        String targetWord,
        @NonNull
        Familiarity familiarity) implements TranslationDetails {
    public DictionaryTranslationDetails {
        EmptyValidator emptyValidator = (String... fields) -> {
            Arrays.stream(fields).forEach(field -> {
                if(field.isEmpty()) throw new IllegalArgumentException("Field cannot be empty");
            });
        };

        final Pattern INVALID_CHARS = Pattern.compile("[%&/^!<>@#$'\"*;`:=\\-_+.,(){}\\[\\]?\\\\1234567890]");

        PatternValidator patternValidator = (Pattern pattern, String field) -> {
            if(pattern.matcher(field).find()) {
                throw new IllegalArgumentException(field + " contains invalid characters");
            }
        };

        LengthValidator maxLengthValidator = (int length, String field) -> {
            if(field.length() > length) throw new IllegalArgumentException("Field cannot be longer than " + length + " characters");
        };

        EqualEnumValueValidator<Language> equalValueValidator = (Language lang1, Language lang2) -> {
            if(lang1.equals(lang2)) throw new IllegalArgumentException("source language and target language cannot be equal");
        };
        emptyValidator.validate(sourceWord, targetWord, familiarity.name());

        sourceWord = sourceWord.trim();
        targetWord = targetWord.trim();

        patternValidator.validate(INVALID_CHARS, sourceWord);
        patternValidator.validate(INVALID_CHARS, targetWord);

        maxLengthValidator.validate(50, sourceWord);
        maxLengthValidator.validate(50, targetWord);
    }
}
