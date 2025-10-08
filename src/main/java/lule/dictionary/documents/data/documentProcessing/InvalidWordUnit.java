package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.entity.EmptyTranslation;
import lule.dictionary.translations.data.entity.Translation;

public record InvalidWordUnit(int id,
                              boolean isPhrasePart,
                              @NonNull String rawText,
                              Translation translation) implements WordUnit {
    public static InvalidWordUnit of(int id,
                                     @NonNull String rawText) {
        return new InvalidWordUnit(id, false, rawText, EmptyTranslation.of());
    }
}
