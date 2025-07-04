package lule.dictionary.entity.application.implementation.imports;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import lule.dictionary.service.language.Language;
import lule.dictionary.functionalInterface.validation.EmptyValidator;
import lule.dictionary.functionalInterface.validation.LengthValidator;
import lule.dictionary.functionalInterface.validation.PatternValidator;

import java.util.Arrays;
import java.util.regex.Pattern;

@Builder(toBuilder = true)
public record ImportWithIdImp(
        @NonNull
        String title,
        @NonNull
        String content,
        @NonNull
        String url,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String owner,
        int id) implements ImportWithId {

        public ImportWithIdImp {
                EmptyValidator emptyValidator = (String... fields) -> Arrays.stream(fields).forEach(field -> {
                        if(field.isEmpty()) throw new IllegalArgumentException("Field cannot be empty");
                });

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

                emptyValidator.validate(title, content, owner);

                title = title.trim();
                content = content.trim();
                url = url.trim();
                owner = owner.trim();

                patternValidator.validate(INVALID_FIRST_CHAR, title);

                maxLengthValidator.validate(50, owner);
                maxLengthValidator.validate(100, title);
                maxLengthValidator.validate(20000, content);
                maxLengthValidator.validate(200, url);
        }
}
