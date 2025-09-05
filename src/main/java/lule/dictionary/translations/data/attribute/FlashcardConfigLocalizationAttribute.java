package lule.dictionary.translations.data.attribute;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FlashcardConfigLocalizationAttribute(@NonNull String familiarityText,
                                                   @NonNull String howManyText,
                                                   @NonNull String phrasesText,
                                                   @NonNull String wordsText,
                                                   @NonNull String reviewTranslationsText) {
}
