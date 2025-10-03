package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.entity.Translation;

@With
public record PhraseUnit(int id,
                         @NonNull Translation translation,
                         @NonNull String rawText,
                         int size) implements DocumentUnit {

    public static PhraseUnit of(int id, Translation translation, String rawText, int size) {
        return new PhraseUnit(id, translation, rawText, size);
    }
}
