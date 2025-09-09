package lule.dictionary.documents.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

import java.util.List;
import java.util.Map;

@Builder
public record DocumentPageData(@NonNull String title,
                               @NonNull List<Paragraph> content,
                               @NonNull Map<String, Translation> translations,
                               int documentId,
                               int selectedWordId) {
    @Override
    public Map<String, Translation> translations() {
        return Map.copyOf(translations);
    }

    @Override
    public List<Paragraph> content() {
        return List.copyOf(content);
    }
}
