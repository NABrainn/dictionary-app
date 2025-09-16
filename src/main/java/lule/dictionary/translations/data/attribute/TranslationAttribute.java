package lule.dictionary.translations.data.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.TranslationFormType;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.TranslationLocalizationKey;

import java.util.Collections;
import java.util.Map;

@Builder
public record TranslationAttribute(int documentId,
                                   int selectedWordId,
                                   int translationId,
                                   int currentFamiliarity,
                                   boolean isPhrase,
                                   @NonNull Map<Integer, Familiarity> familiarityLevels,
                                   @NonNull Map<TranslationLocalizationKey, String> localization,
                                   @NonNull Translation translation,
                                   TranslationFormType type) {
    public static final int UNINITIALIZED_ID = -1;

    public TranslationAttribute {
        if (translationId == 0) {
            translationId = UNINITIALIZED_ID;
        }

        familiarityLevels = Collections.unmodifiableMap(familiarityLevels);
    }

    @Override
    public Map<Integer, Familiarity> familiarityLevels() {
        return Collections.unmodifiableMap(familiarityLevels);
    }

    @Override
    public TranslationFormType type() {
        if(type == null) {
            throw new NullPointerException("Operation type not implemented");
        }
        return type;
    }
}
