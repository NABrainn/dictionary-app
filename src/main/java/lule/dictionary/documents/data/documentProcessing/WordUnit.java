package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record WordUnit(@NonNull Translation translation,
                       boolean isPhrasePart) implements DocumentUnit {

    public static WordUnit of(Translation translation, boolean isPhrasePart) {
        return new WordUnit(translation, isPhrasePart);
    }
}
