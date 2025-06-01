package lule.dictionary.dto.application.implementation.imports;

import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.functionalInterface.validation.EmptyValidator;
import lule.dictionary.functionalInterface.validation.LengthValidator;
import lule.dictionary.functionalInterface.validation.PatternValidator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public record DictionaryImportDetails(
        @NonNull String title,
        @NonNull String content,
        String url) implements ImportDetails {
    public DictionaryImportDetails {
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

        emptyValidator.validate(title, content);

        title = title.trim();
        content = content.trim();
        url = url.trim();

        patternValidator.validate(INVALID_FIRST_CHAR, title);

        maxLengthValidator.validate(100, title);
        maxLengthValidator.validate(10000, content);
        maxLengthValidator.validate(200, url);
    }
    @Override
    public List<String> importDetailsSummary() {
        return List.of(title, content, url);
    }
}
