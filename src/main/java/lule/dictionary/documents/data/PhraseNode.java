package lule.dictionary.documents.data;

import lombok.NonNull;

import java.util.List;

public record PhraseNode(@NonNull List<WordNode> wordNodes) {
    public static PhraseNode of(List<WordNode> wordNodes) {
        return new PhraseNode(wordNodes);
    }

    @Override
    public List<WordNode> wordNodes() {
        return List.copyOf(wordNodes);
    }
}
