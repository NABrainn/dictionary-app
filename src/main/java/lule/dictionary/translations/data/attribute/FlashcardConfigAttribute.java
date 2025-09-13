package lule.dictionary.translations.data.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.TranslationLocalizationKey;

import java.util.Map;

@Builder
public record FlashcardConfigAttribute(int familiarity,
                                       int quantity,
                                       boolean isPhrase,
                                       @NonNull Map<TranslationLocalizationKey, String> localization) {
}
