package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.entity.Translation;

@With
public record PersistedWordUnit(int id,
                                @NonNull Translation translation,
                                @NonNull String rawText,
                                boolean isPhrasePart) implements WordUnit {

    public static PersistedWordUnit of(Translation translation, String rawText, boolean isPhrasePart) {
        return new PersistedWordUnit(0, translation, rawText, isPhrasePart);
    }

    @Override
    public DocumentUnit withId(int id) {
        return new PersistedWordUnit(id, translation, rawText, isPhrasePart);
    }
}
