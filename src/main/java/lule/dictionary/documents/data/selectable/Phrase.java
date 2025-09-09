package lule.dictionary.documents.data.selectable;

import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;

import java.util.Arrays;
import java.util.List;

public record Phrase(@NonNull List<@NonNull String> targetWords,
                     Familiarity familiarity,
                     int id) implements Selectable {
    public static Phrase of(List<String> phrase, Familiarity familiarity, int id) {
        return new Phrase(phrase, familiarity, id);
    }

    public static Phrase process(String phrase, Familiarity familiarity, int id) {
        List<String> processedPhrase = Arrays.stream(phrase
                        .replace("ph<", "")
                        .replace(">", "")
                        .replace("-", " ")
                        .substring(2)
                        .split(" "))
                .toList();
        return new Phrase(processedPhrase, familiarity, id);
    }

    @Override
    public List<String> targetWords() {
        return List.copyOf(targetWords);
    }

    @Override
    public Familiarity familiarity() {
        if(familiarity == null) throw new NullPointerException("Illegal access: value is null");
        return familiarity;
    }
}
