package lule.dictionary.dto.database.implementation.imports;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.base.Document;
import lule.dictionary.service.language.Language;
import lule.dictionary.functionalInterface.validation.EmptyValidator;
import lule.dictionary.functionalInterface.validation.LengthValidator;
import lule.dictionary.functionalInterface.validation.PatternValidator;

import java.util.Arrays;
import java.util.regex.Pattern;

@Builder
public record ImportWithPaginationImp(@NonNull
                                      String title,
                                      @NonNull
                                      String url,
                                      @NonNull
                                      Language sourceLanguage,
                                      @NonNull
                                      Language targetLanguage,
                                      @NonNull
                                      String owner,
                                      @NonNull
                                      String pageContent,
                                      int totalContentLength) implements Document {
    public ImportWithPaginationImp {
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

        emptyValidator.validate(title, pageContent);

        title = title.trim();
        pageContent = pageContent.trim();
        url = url.trim();

        patternValidator.validate(INVALID_FIRST_CHAR, title);

        maxLengthValidator.validate(200, title);
        maxLengthValidator.validate(100000, pageContent);
        maxLengthValidator.validate(200, url);
    }
}
