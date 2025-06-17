package lule.dictionary.service.translation.dto;

import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;

import java.util.Map;

public record TranslationModel(int importId, int currentFamiliarity, Translation translation, Map<Integer, Familiarity> familiarityLevels, int selectedWordId) {
}
