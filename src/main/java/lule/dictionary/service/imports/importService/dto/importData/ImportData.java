package lule.dictionary.service.imports.importService.dto.importData;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.application.ContentData;
import lule.dictionary.dto.database.interfaces.translation.Translation;

import java.util.Map;

@Builder
public record ImportData(@NonNull String title,
                         @NonNull ContentData content,
                         @NonNull Map<String, Translation> translations,
                         int importId,
                         int selectedWordId) {
    @Override
    public @NonNull Map<String, Translation> translations() {
        return Map.copyOf(translations);
    }
}
