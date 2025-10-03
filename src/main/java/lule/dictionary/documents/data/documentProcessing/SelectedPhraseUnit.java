package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.entity.Translation;

@With
public record SelectedPhraseUnit(int id,
                                 int endId,
                                 @NonNull Translation translation,
                                 @NonNull String rawText) implements SelectedUnit {
    public static SelectedPhraseUnit of(int id, int endId, Translation translation, String rawText) {
        return new SelectedPhraseUnit(id, endId, translation, rawText);
    }
}
