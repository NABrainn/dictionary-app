package lule.dictionary.dto.application;

import lule.dictionary.service.imports.importService.dto.Selectable;

import java.util.List;

public record ContentData(List<List<Selectable>> paragraphs,
                          List<Integer> startIndices) {
    public static ContentData of(List<List<Selectable>> paragraphs, List<Integer> startIndices) {
        return new ContentData(paragraphs, startIndices);
    }

    @Override
    public List<List<Selectable>> paragraphs() {
        return List.copyOf(paragraphs);
    }

    @Override
    public List<Integer> startIndices() {
        return List.copyOf(startIndices);
    }
}
