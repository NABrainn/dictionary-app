package lule.dictionary.dto.application;

import java.util.List;

public record ImportContentData(List<List<String>> paragraphs,
                                List<Integer> startIndices) {
    @Override
    public List<List<String>> paragraphs() {
        return List.copyOf(paragraphs);
    }

    @Override
    public List<Integer> startIndices() {
        return List.copyOf(startIndices);
    }
}
