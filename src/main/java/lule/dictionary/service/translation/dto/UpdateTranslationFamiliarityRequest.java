package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.functionalInterface.validation.PatternValidator;

import java.util.regex.Pattern;

public record UpdateTranslationFamiliarityRequest(@NotBlank
                                                @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                                String targetWord,
                                                @NonNull Familiarity familiarity,
                                                @NonNull Language sourceLanguage,
                                                @NonNull Language targetLanguage,
                                                @NonNull String owner,
                                                int importId,
                                                int selectedWordId) {
    public UpdateTranslationFamiliarityRequest {
        final Pattern INVALID_CHARS = Pattern.compile("[%&/^!<>@#$'\"*;`:=\\-_+.,(){}\\[\\]?\\\\]");

        PatternValidator patternValidator = (Pattern pattern, String field) -> {
            if(pattern.matcher(field).find()) {
                throw new IllegalArgumentException(field + " contains invalid characters");
            }
        };
        patternValidator.validate(INVALID_CHARS, targetWord);
    }
}
