package lule.dictionary.controller.translation.dto;

import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

public record UpdateFamiliarityRequest(String targetWord, Familiarity familiarity, String sourceWord, Language sourceLanguage, Language targetLanguage, String owner, int importId, int selectedWordId) {
}
