package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.Max;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

@Builder
public record MutateTranslationRequest(
        @NonNull @Max(value = 50, message = "Source word cannot be longer than 50 characters") String sourceWord,
        @NonNull @Max(value = 50, message = "Target word cannot be longer than 50 characters") String targetWord,
        @NonNull Familiarity familiarity,
        @NonNull Language sourceLanguage,
        @NonNull Language targetLanguage,
        @NonNull String owner,
        int importId,
        int selectedWordId) {
}
