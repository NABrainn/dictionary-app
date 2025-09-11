package lule.dictionary.translations.data.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.TranslationLocalizationKey;

import java.util.List;
import java.util.Map;

@Builder
public record BaseFlashcardAttribute(@NonNull List<Translation> translations,
                                     @NonNull Map<TranslationLocalizationKey, String> localization,
                                     int id,
                                     int size,
                                     int familiarity,
                                     int quantity,
                                     boolean isPhrase) {
    @Override
    public List<Translation> translations() {
        return List.copyOf(translations);
    }
}
