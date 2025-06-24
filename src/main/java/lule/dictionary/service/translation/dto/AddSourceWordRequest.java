package lule.dictionary.service.translation.dto;

public record AddSourceWordRequest(
        String sourceWord, int importId,
        String targetWord, int selectedWordId) {
}
