package lule.dictionary.service.translation.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

@Builder
public record UpdateFamiliarityRequest(
        @NonNull String targetWord,
        @NonNull Familiarity familiarity,
        @NonNull String sourceWord,
        @NonNull Language sourceLanguage,
        @NonNull Language targetLanguage,
        @NonNull String owner,
        int importId,
        int selectedWordId) {
}
