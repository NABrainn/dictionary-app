package lule.dictionary.translations.data.request;

import lombok.NonNull;

import java.util.List;

public record GetCardAttributeRequest(@NonNull List<String> sourceWord,
                                      @NonNull String targetWord) {
    public static GetCardAttributeRequest of(List<String> sourceWord, String targetWord) {
        return new GetCardAttributeRequest(sourceWord, targetWord);
    }
}
