package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.entity.Translation;

@With
public record NewWordUnit(int id,
                          @NonNull Translation translation,
                          @NonNull String rawText,
                          boolean isPhrasePart) implements WordUnit {

    public static NewWordUnit of(Translation translation, String rawText, boolean isPhrasePart) {
        return new NewWordUnit(0, translation, rawText, isPhrasePart);
    }

    @Override
    public DocumentUnit withId(int id) {
        return new NewWordUnit(id, translation, rawText, isPhrasePart);
    }
}
