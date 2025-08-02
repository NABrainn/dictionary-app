package lule.dictionary.service.imports.importService.dto;

import java.util.List;

public record Phrase(List<String> phrase) implements Selectable {
    public static Phrase of(List<String> phrase) {
        return new Phrase(phrase);
    }

    @Override
    public List<String> phrase() {
        return List.copyOf(phrase);
    }
}
