package lule.dictionary.dto.database.implementation.translation.base;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.language.Language;
import lule.dictionary.functionalInterface.validation.EmptyValidator;
import lule.dictionary.functionalInterface.validation.EqualEnumValueValidator;
import lule.dictionary.functionalInterface.validation.LengthValidator;

import java.util.List;

@Builder(toBuilder = true)
public record TranslationImp(
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
        String owner,
        boolean isPhrase) implements Translation {

        public TranslationImp {
                EmptyValidator emptyValidator = (String... fields) -> {
                        for(var field : fields) {
                                if(field.isEmpty()) throw new IllegalArgumentException("Field cannot be empty");
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
                        maxLengthValidator.validate(200, word);
                }

                emptyValidator.validate(targetWord, owner, familiarity.name());

                targetWord = targetWord.trim();
                owner = owner.trim();

                maxLengthValidator.validate(50, owner);
                maxLengthValidator.validate(200, targetWord);
        }
}
