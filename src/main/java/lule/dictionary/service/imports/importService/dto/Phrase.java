package lule.dictionary.service.imports.importService.dto;

import lule.dictionary.enumeration.Familiarity;

import java.util.List;

public record Phrase(List<String> targetWords,
                     Familiarity familiarity) implements Selectable {
    public static Phrase of(List<String> phrase, Familiarity familiarity) {
        return new Phrase(phrase, familiarity);
    }

    @Override
    public List<String> targetWords() {
        return List.copyOf(targetWords);
    }
}
