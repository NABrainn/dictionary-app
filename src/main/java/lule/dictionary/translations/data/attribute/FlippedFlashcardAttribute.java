package lule.dictionary.translations.data.attribute;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record FlippedFlashcardAttribute(int id,
                                        int size,
                                        boolean hidden,
                                        @NonNull List<String> sourceWord,
                                        @NonNull String targetWord) {
    public List<String> sourceWord() {
        return List.copyOf(sourceWord);
    }
}
