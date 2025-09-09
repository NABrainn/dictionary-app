package lule.dictionary.documents.data;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

import java.util.List;

public record PhraseNode(@NonNull List<WordNode> wordNodes,
                         @NonNull Translation phrase) {
    public static PhraseNode fromWordNodes(List<WordNode> wordNodes, Translation phrase) {
        List<WordNode> renderedPhrase = wordNodes.stream()
                .map(wordNode -> wordNodes.getFirst().equals(wordNode) ?
                        wordNode.withRenderedText("ph<" + (phrase.familiarity().ordinal()) + "<" + wordNode.renderedText()) :
                        wordNodes.getLast().equals(wordNode) ?
                                wordNode.withRenderedText(wordNode.renderedText() + ">>") :
                                wordNode
                )
                .toList();
        return new PhraseNode(renderedPhrase, phrase);
    }

    @Override
    public List<WordNode> wordNodes() {
        return List.copyOf(wordNodes);
    }
}
