package lule.dictionary.translations.controller.vocabulary.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

import java.util.List;

@Builder
public record BaseFlashcardAttribute(@NonNull List<Translation> translations,
                                     int id,
                                     int size,
                                     int familiarity,
                                     int quantity,
                                     boolean isPhrase) {
    @Override
    public List<Translation> translations() {
        return List.copyOf(translations);
    }
}
