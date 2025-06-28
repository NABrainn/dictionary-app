package lule.dictionary.service.imports.importPageService.dto;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.translation.Translation;

import java.util.List;
import java.util.Map;

public record ImportModel(@NonNull String title,
                          List<String> content,
                          @NonNull Map<String, Translation> translations,
                          int importId,
                          int selectedWordId,
                          int currentPage,
                          int pages,
                          List<List<Integer>> rows,
                          int currentRow,
                          int firstPageOfRow) {
    @Override
    public List<String> content() {
        return List.copyOf(content);
    }
    @Override
    public List<List<Integer>> rows() {
        return List.copyOf(rows);
    }
    @Override
    public @NonNull Map<String, Translation> translations() {
        return Map.copyOf(translations);
    }
}
