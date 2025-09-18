package lule.dictionary.translations.data.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.TranslationFormType;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.Familiarity;

import java.util.Collections;
import java.util.Map;

@Builder
public record TranslationAttribute(@NonNull Map<Integer, Familiarity> familiarityLevels,
                                   @NonNull Translation translation,
                                   int documentId,
                                   int id,
                                   int currentFamiliarity,
                                   boolean isPhrase,
                                   boolean isPersisted,
                                   TranslationFormType type) {
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
