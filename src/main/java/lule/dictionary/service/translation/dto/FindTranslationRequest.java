package lule.dictionary.service.translation.dto;

public record FindTranslationRequest(int importId, String targetWord, int selectedWordId) {
}
