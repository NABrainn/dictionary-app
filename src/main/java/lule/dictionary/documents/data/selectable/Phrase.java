package lule.dictionary.documents.data.selectable;

import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;

import java.util.List;

public record Phrase(@NonNull List<@NonNull String> targetWords,
                     Familiarity familiarity) implements Selectable {
    public static Phrase of(List<String> phrase, Familiarity familiarity) {
        return new Phrase(phrase, familiarity);
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
