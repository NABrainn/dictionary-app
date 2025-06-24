package lule.dictionary.service.imports.importPageService.dto;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.translation.Translation;

import java.util.List;
import java.util.Map;

public record ImportPageModel(@NonNull String title,
                              List<String> content,
                              @NonNull Map<String, Translation> translations,
                              int importId,
                              int selectedWordId) {
    @Override
    public List<String> content() {
        return List.copyOf(content);
    }
    @Override
    public Map<String, Translation> translations() {
        return Map.copyOf(translations);
    }
}
