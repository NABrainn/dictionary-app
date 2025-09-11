package lule.dictionary.translations.data.attribute;

import lombok.NonNull;

import java.util.List;

public record WordCardAttribute(@NonNull List<String> sourceWord,
                                @NonNull String targetWord) {
    public static WordCardAttribute of(List<String> sourceWord, String targetWord) {
        return new WordCardAttribute(sourceWord, targetWord);
    }
}
