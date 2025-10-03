package lule.dictionary.documents.data.documentProcessing;

import lule.dictionary.translations.data.entity.Translation;

import java.util.List;
import java.util.Optional;

public record Phrases(List<Translation> phrases) {
    public static Phrases of(List<Translation> phrases) {
        return new Phrases(phrases);
    }

    public boolean containsWord(String input) {
        return phrases.stream()
                .anyMatch(phrase -> phrase
                        .processedTargetWord()
                        .contains(input));
    }

    public Optional<Translation> containsPhrase(String phrase) {
        return phrases.stream()
                .filter(translation -> translation.processedTargetWord()
                        .trim()
                        .equalsIgnoreCase(phrase))
                .findFirst();
    }
}
