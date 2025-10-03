package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.entity.Translation;

@With
public record SelectedWordUnit(int id,
                               @NonNull Translation translation,
                               @NonNull String rawText,
                               boolean isPersisted) implements SelectedUnit {
    public static SelectedWordUnit of(int id, Translation translation, String rawText, boolean persisted) {
        return new SelectedWordUnit(id, translation, rawText, persisted);
    }
}
