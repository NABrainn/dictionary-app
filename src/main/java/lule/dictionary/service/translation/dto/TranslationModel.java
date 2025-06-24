package lule.dictionary.service.translation.dto;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;

import java.util.Map;

public record TranslationModel(int importId,
                               int currentFamiliarity,
                               Translation translation,
                               @NonNull Map<Integer, Familiarity> familiarityLevels,
                               int selectedWordId) {
    @Override
    public Map<Integer, Familiarity> familiarityLevels() {
        return Map.copyOf(familiarityLevels);
    }
}
