package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record SelectedWordUnit(int id,
                               @NonNull Translation translation,
                               @NonNull String rawText) implements SelectedUnit {
    public static SelectedWordUnit of(int id, Translation translation, String rawText) {
        return new SelectedWordUnit(id, translation, rawText);
    }
    @Override
    public SelectedUnit withId(int id) {
        return new SelectedWordUnit(id, translation, rawText);
    }
}
