package lule.dictionary.controller.translation.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

@Builder
public record AddTranslationRequest(
        @NonNull String sourceWord,
        @NonNull String targetWord,
        @NonNull Familiarity familiarity,
        @NonNull Language sourceLanguage,
        @NonNull Language targetLanguage,
        @NonNull String owner,
        int importId,
        int selectedWordId) {
}
