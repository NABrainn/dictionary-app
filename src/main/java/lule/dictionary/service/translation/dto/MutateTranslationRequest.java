package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

@Builder
public record MutateTranslationRequest(
        @NotBlank
        @Size(max = 200, message = "Source word cannot be longer than 200 characters")
        String sourceWord,
        @NotBlank
        @Size(max = 200, message = "Target word cannot be longer than 200 characters")
        String targetWord,
        @NonNull Familiarity familiarity,
        @NonNull Language sourceLanguage,
        @NonNull Language targetLanguage,
        @NonNull String owner,
        int importId,
        int selectedWordId) {
}
