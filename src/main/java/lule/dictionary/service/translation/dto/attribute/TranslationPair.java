package lule.dictionary.service.translation.dto.attribute;

import java.util.List;

public record TranslationPair(List<String> sourceWords, String targetWord) {
    public static TranslationPair of(List<String> sourceWords, String targetWord) {
        return new TranslationPair(sourceWords, targetWord);
    }
}
