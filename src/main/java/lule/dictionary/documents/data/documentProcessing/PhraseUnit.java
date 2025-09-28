package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record PhraseUnit(@NonNull Translation translation,
                         @NonNull String rawText,
                         boolean isPhrasePart) implements DocumentUnit {

    public static DocumentUnit of(String wordFromText, Translation translation) {
        return new PhraseUnit(translation, wordFromText, false);
    }
}
