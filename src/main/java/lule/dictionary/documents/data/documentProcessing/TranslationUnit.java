package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record TranslationUnit(@NonNull Translation translation,
                              @NonNull String rawText,
                              boolean isPhrasePart) implements DocumentUnit {

    public static TranslationUnit of(Translation translation, String wordFromText, boolean isPhrasePart) {
        return new TranslationUnit(translation, wordFromText, isPhrasePart);
    }
}
