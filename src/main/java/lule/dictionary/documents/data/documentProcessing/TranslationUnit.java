package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record TranslationUnit(@NonNull Translation translation,
                              boolean isPhrasePart) implements DocumentUnit {

    public static TranslationUnit of(Translation translation, boolean isPhrasePart) {
        return new TranslationUnit(translation, isPhrasePart);
    }
}
