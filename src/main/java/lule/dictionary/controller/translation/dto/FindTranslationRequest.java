package lule.dictionary.controller.translation.dto;

public record FindTranslationRequest(int importId, String targetWord, int selectedWordId) {
}
