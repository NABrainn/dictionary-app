package lule.dictionary.documents.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.DocumentUnit;
import lule.dictionary.translations.data.Translation;

import java.util.List;
import java.util.Map;

@Builder
public record DocumentContentData(@NonNull String title,
                                  @NonNull List<DocumentUnit> content,
                                  @NonNull Map<String, Translation> translations,
                                  int documentId) {
    @Override
    public Map<String, Translation> translations() {
        return Map.copyOf(translations);
    }

    @Override
    public List<DocumentUnit> content() {
        return List.copyOf(content);
    }
}
