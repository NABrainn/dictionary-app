package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.functionalInterface.validation.PatternValidator;

public record UpdateTranslationFamiliarityRequest(@NotBlank
                                                @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                                @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Source word contains invalid characters")
                                                String targetWord,
                                                @NonNull Familiarity familiarity,
                                                @NonNull Language sourceLanguage,
                                                @NonNull Language targetLanguage,
                                                @NonNull String owner,
                                                int importId,
                                                int selectedWordId) {
}
