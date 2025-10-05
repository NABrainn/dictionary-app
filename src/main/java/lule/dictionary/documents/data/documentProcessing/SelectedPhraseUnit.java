package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.entity.Translation;

@With
public record SelectedPhraseUnit(int id,
                                 boolean isPersisted,
                                 @NonNull Translation translation,
                                 @NonNull String rawText) implements SelectedUnit {
    public static SelectedPhraseUnit of(int id, boolean isPersisted, Translation translation, String rawText) {
        return new SelectedPhraseUnit(id, isPersisted, translation, rawText);
    }
}
