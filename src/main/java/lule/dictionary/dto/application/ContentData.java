package lule.dictionary.dto.application;

import java.util.List;

public record ContentData(List<List<String>> paragraphs,
                          List<Integer> startIndices) {
    public static ContentData of(List<List<String>> paragraphs, List<Integer> startIndices) {
        return new ContentData(paragraphs, startIndices);
    }

    @Override
    public List<List<String>> paragraphs() {
        return List.copyOf(paragraphs);
    }

    @Override
    public List<Integer> startIndices() {
        return List.copyOf(startIndices);
    }
}
