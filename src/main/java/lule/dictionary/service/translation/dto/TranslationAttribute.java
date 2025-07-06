package lule.dictionary.service.translation.dto;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;

import java.util.Collections;
import java.util.Map;

public record TranslationAttribute(int importId,
                                   int currentFamiliarity,
                                   Translation translation,
                                   @NonNull Map<Integer, Familiarity> familiarityLevels,
                                   int selectedWordId,
                                   int page) {
    @Override
    public Map<Integer, Familiarity> familiarityLevels() {
        return Collections.unmodifiableMap(familiarityLevels);
    }
}
