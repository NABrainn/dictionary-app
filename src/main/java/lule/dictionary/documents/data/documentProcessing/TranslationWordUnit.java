package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record TranslationWordUnit(@NonNull Translation translation,
                                  @NonNull String rawText,
                                  boolean isPhrasePart) implements DocumentUnit, NonPhraseUnit {

    public static TranslationWordUnit of(Translation translation, String wordFromText, boolean isPhrasePart) {
        return new TranslationWordUnit(translation, wordFromText, isPhrasePart);
    }
}
