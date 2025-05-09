package lule.dictionary.entity;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.validation.EmptyValidator;
import lule.dictionary.validation.EqualEnumValueValidator;
import lule.dictionary.validation.LengthValidator;
import lule.dictionary.validation.PatternValidator;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

import java.util.Arrays;
import java.util.regex.Pattern;

@Slf4j
public record Translation(
        @NonNull
        String sourceWord,
        @NonNull
        String targetWord,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String translationOwner,
        @NonNull
        Familiarity familiarity) {
    public Translation {
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
        emptyValidator.validate(sourceWord, targetWord, sourceLanguage.name(), targetLanguage.name(), translationOwner, familiarity.name());

        sourceWord = sourceWord.trim();
        targetWord = targetWord.trim();
        translationOwner = translationOwner.trim();

        patternValidator.validate(INVALID_CHARS, sourceWord);
        patternValidator.validate(INVALID_CHARS, targetWord);
        patternValidator.validate(INVALID_CHARS, translationOwner);

        maxLengthValidator.validate(50, sourceWord);
        maxLengthValidator.validate(50, targetWord);
        maxLengthValidator.validate(20, translationOwner);

        equalValueValidator.validate(sourceLanguage, targetLanguage);
    }
}
