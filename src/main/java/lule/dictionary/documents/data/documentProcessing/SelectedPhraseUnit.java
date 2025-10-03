package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.entity.Translation;

public record SelectedPhraseUnit(int id,
                                 @NonNull Translation translation,
                                 @NonNull String rawText) implements SelectedUnit {
    public static SelectedPhraseUnit of(int id, Translation translation, String rawText) {
        return new SelectedPhraseUnit(id, translation, rawText);
    }
    @Override
    public DocumentUnit withId(int id) {
        return new SelectedPhraseUnit(id, translation, rawText);
    }
}
