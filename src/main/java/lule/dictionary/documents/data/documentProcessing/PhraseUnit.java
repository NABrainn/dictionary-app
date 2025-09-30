package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record PhraseUnit(@NonNull Translation translation,
                         @NonNull String rawText) implements DocumentUnit {

    public static DocumentUnit of(Translation translation, String wordFromText) {
        return new PhraseUnit(translation, wordFromText);
    }
}
