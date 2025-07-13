package lule.dictionary.service.imports.importService.dto.importData;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.translation.Translation;

import java.util.List;
import java.util.Map;

@Builder
public record ImportData(@NonNull String title,
                         @NonNull List<String> content,
                         @NonNull Map<String, Translation> translations,
                         int importId,
                         int selectedWordId) {
    @Override
    public List<String> content() {
        return List.copyOf(content);
    }
    @Override
    public @NonNull Map<String, Translation> translations() {
        return Map.copyOf(translations);
    }
}
