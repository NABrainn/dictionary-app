package lule.dictionary.entity.application.implementation.translation.base;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.functionalInterface.validation.EmptyValidator;
import lule.dictionary.functionalInterface.validation.EqualEnumValueValidator;
import lule.dictionary.functionalInterface.validation.LengthValidator;
import lule.dictionary.functionalInterface.validation.PatternValidator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Builder(toBuilder = true)
public record DictionaryTranslation(
        @NonNull
        List<String> sourceWords,
        @NonNull
        String targetWord,
        @NonNull
        Familiarity familiarity,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String owner) implements Translation {

        public DictionaryTranslation {
                EmptyValidator emptyValidator = (String... fields) -> {
                        for(var field : fields) {
                                if(field.isEmpty()) throw new IllegalArgumentException("Field cannot be empty");
                        }
                };

                final Pattern INVALID_CHARS = Pattern.compile("[%&/^!<>@#$'\"*;`:=\\-_+.,(){}\\[\\]?\\\\]");

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

                for(var word : sourceWords) {
                        emptyValidator.validate(word);
                        patternValidator.validate(INVALID_CHARS, word);
                        maxLengthValidator.validate(200, word);
                }

                emptyValidator.validate(targetWord, owner, familiarity.name());

                targetWord = targetWord.trim();
                owner = owner.trim();

                patternValidator.validate(INVALID_CHARS, targetWord);

                maxLengthValidator.validate(50, owner);
                maxLengthValidator.validate(200, targetWord);
        }
}
