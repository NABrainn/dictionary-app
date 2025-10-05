package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.entity.Translation;

@With
public record SelectedWordUnit(int id,
                               boolean isPersisted,
                               @NonNull Translation translation,
                               @NonNull String rawText) implements SelectedUnit {
    public static SelectedWordUnit of(int id, boolean isPersisted, Translation translation, String rawText) {
        return new SelectedWordUnit(id, isPersisted, translation, rawText);
    }
}
