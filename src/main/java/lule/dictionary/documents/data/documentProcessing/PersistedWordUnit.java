package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.entity.Translation;

public record PersistedWordUnit(int id,
                                @NonNull Translation translation,
                                @NonNull String rawText,
                                boolean isPhrasePart) implements WordUnit {

    public static PersistedWordUnit of(int id, Translation translation, String rawText, boolean isPhrasePart) {
        return new PersistedWordUnit(id, translation, rawText, isPhrasePart);
    }
}
