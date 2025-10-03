package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.entity.Translation;

public record PhraseUnit(int id,
                         @NonNull Translation translation,
                         @NonNull String rawText,
                         int size) implements DocumentUnit {

    public static PhraseUnit of(int id, Translation translation, String rawText, int size) {
        return new PhraseUnit(id, translation, rawText, size);
    }
    @Override
    public DocumentUnit withId(int id) {
        return new PhraseUnit(id, translation, rawText, size);
    }
}
