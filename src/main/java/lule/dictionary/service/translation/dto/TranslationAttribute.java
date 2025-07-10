package lule.dictionary.service.translation.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;

import java.util.Collections;
import java.util.Map;

@Builder
public record TranslationAttribute(int importId,
                                   int selectedWordId,
                                   int translationId,
                                   Translation translation,
                                   int currentFamiliarity,
                                   @NonNull Map<Integer, Familiarity> familiarityLevels,
                                   int page) {
    @Override
    public Map<Integer, Familiarity> familiarityLevels() {
        return Collections.unmodifiableMap(familiarityLevels);
    }
}
