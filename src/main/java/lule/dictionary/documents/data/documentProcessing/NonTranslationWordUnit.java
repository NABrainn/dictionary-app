package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record NonTranslationWordUnit(int id,
                                     @NonNull Translation translation,
                                     @NonNull String rawText,
                                     boolean isPhrasePart) implements WordUnit {

    public static NonTranslationWordUnit of(Translation translation, String rawText, boolean isPhrasePart) {
        return new NonTranslationWordUnit(0, translation, rawText, isPhrasePart);
    }

    @Override
    public DocumentUnit withId(int id) {
        return new NonTranslationWordUnit(id, translation, rawText, isPhrasePart);
    }
}
