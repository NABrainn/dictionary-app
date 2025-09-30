package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record NonTranslationWordUnit(@NonNull Translation translation,
                                     @NonNull String rawText,
                                     boolean isPhrasePart) implements DocumentUnit, NonPhraseUnit {

    public static NonTranslationWordUnit of(Translation translation, String wordFromText, boolean isPhrasePart) {
        return new NonTranslationWordUnit(translation, wordFromText, isPhrasePart);
    }
}
