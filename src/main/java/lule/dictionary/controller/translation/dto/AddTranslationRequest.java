package lule.dictionary.controller.translation.dto;

import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

public record AddTranslationRequest(String sourceWord, String targetWord, Familiarity familiarity, Language sourceLanguage, Language targetLanguage, String owner, int importId, int selectedWordId) {
}
