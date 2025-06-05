package lule.dictionary.service.application;

import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.translation.Translation;

import java.util.List;
import java.util.Map;

public record ImportData(@NonNull String title, List<String> content, @NonNull Map<String, Translation> translations, int importId) {
    @Override
    public Map<String, Translation> translations() {
        return Map.copyOf(translations);
    }
    @Override
    public List<String> content() {
        return List.copyOf(content);
    }
}