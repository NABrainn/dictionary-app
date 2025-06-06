package lule.dictionary.controller.imports.dto;

import lule.dictionary.dto.application.interfaces.translation.Translation;

import java.util.List;
import java.util.Map;

public record DictionaryModel(String title, List<String> content, Map<String, Translation> translations, int importId, int selectedWordId) {
}
