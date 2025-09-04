package lule.dictionary.documents.service.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

import java.util.Map;

@Builder
public record DocumentPageData(@NonNull String title,
                               @NonNull ContentData content,
                               @NonNull Map<String, Translation> translations,
                               int documentId,
                               int selectedWordId) {
    @Override
    public @NonNull Map<String, Translation> translations() {
        return Map.copyOf(translations);
    }
}
