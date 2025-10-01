package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

public record TranslationWordUnit(int id,
                                  @NonNull Translation translation,
                                  @NonNull String rawText,
                                  boolean isPhrasePart) implements WordUnit {

    public static TranslationWordUnit of(Translation translation, String rawText, boolean isPhrasePart) {
        return new TranslationWordUnit(0, translation, rawText, isPhrasePart);
    }

    @Override
    public DocumentUnit withId(int id) {
        return new TranslationWordUnit(id, translation, rawText, isPhrasePart);
    }
}
