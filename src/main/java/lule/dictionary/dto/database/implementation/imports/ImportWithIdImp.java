package lule.dictionary.dto.database.implementation.imports;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.ImportWithId;
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
        String pageContent,
        @NonNull
        String url,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String owner,
        int id,
        int totalContentLength) implements ImportWithId {

        public ImportWithIdImp {
                EmptyValidator emptyValidator = (String... fields) -> Arrays.stream(fields).forEach(field -> {
                        if(field != null) {
                                if(field.isEmpty()) throw new IllegalArgumentException("Field cannot be empty");
                        }
                });

                final Pattern INVALID_FIRST_CHAR = Pattern.compile("[%&/^!<>@#$*;`:=\\-_+.,(){}\\[\\]?\\\\]");

                PatternValidator patternValidator = (Pattern pattern, String field) -> {
                    String first = String.valueOf(field.charAt(0));
                    if(pattern.matcher(first).find()) {
                            throw new IllegalArgumentException(field + " contains invalid characters");
                    }
                };

                LengthValidator maxLengthValidator = (int length, String field) -> {
                        if(field != null) {
                                if(field.length() > length) throw new IllegalArgumentException("Field cannot be longer than " + length + " characters");
                        }
                };

                emptyValidator.validate(title, pageContent, owner);

                title = title.trim();
                if(pageContent != null) {
                        pageContent = pageContent.trim();
                }
                url = url.trim();
                owner = owner.trim();

                patternValidator.validate(INVALID_FIRST_CHAR, title);

                maxLengthValidator.validate(50, owner);
                maxLengthValidator.validate(200, title);
                maxLengthValidator.validate(100000, pageContent);
                maxLengthValidator.validate(200, url);
        }
}
