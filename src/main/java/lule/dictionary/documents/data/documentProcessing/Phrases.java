package lule.dictionary.documents.data.documentProcessing;

import lule.dictionary.translations.data.Translation;

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

    public Optional<Translation> findPhrase(String phrase) {
        return phrases.stream()
                .filter(dbPhrases -> dbPhrases.processedTargetWord().equalsIgnoreCase(phrase))
                .findFirst();
    }
}
