package lule.dictionary.dto;


import lombok.NonNull;
import lule.dictionary.functionalInterface.EmptyValidator;
import lule.dictionary.functionalInterface.EqualEnumValueValidator;
import lule.dictionary.functionalInterface.LengthValidator;
import lule.dictionary.functionalInterface.PatternValidator;
import lule.dictionary.enumeration.Language;

import java.util.Arrays;
import java.util.regex.Pattern;

public record Import(
        @NonNull
        String title,
        @NonNull
        String content,
        String url,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String owner
) {
    public Import {
        EmptyValidator emptyValidator = (String... fields) -> {
            Arrays.stream(fields).forEach(field -> {
                if(field.isEmpty()) throw new IllegalArgumentException("Field cannot be empty");
            });
        };

        final Pattern INVALID_FIRST_CHAR = Pattern.compile("[%&/^!<>@#$*;`:=\\-_+.,(){}\\[\\]?\\\\]");

        PatternValidator patternValidator = (Pattern pattern, String field) -> {
            String first = String.valueOf(field.charAt(0));
            if(pattern.matcher(first).find()) {
                throw new IllegalArgumentException(field + " contains invalid characters");
            }
        };

        LengthValidator maxLengthValidator = (int length, String field) -> {
            if(field.length() > length) throw new IllegalArgumentException("Field cannot be longer than " + length + " characters");
        };

        EqualEnumValueValidator<Language> equalValueValidator = (Language lang1, Language lang2) -> {
            if(lang1.equals(lang2)) throw new IllegalArgumentException("source language and target language cannot be equal");
        };
        emptyValidator.validate(title, content, owner);

        title = title.trim();
        content = content.trim();
        url = url.trim();
        owner = owner.trim();

        patternValidator.validate(INVALID_FIRST_CHAR, title);

        maxLengthValidator.validate(100, title);
        maxLengthValidator.validate(10000, content);
        maxLengthValidator.validate(200, url);
        maxLengthValidator.validate(20, owner);

        equalValueValidator.validate(sourceLanguage, targetLanguage);
    }
}
