package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.NotBlank;

public record AddSourceWordRequest(@NotBlank(message = "source word cannot be blank") String sourceWord, int importId, @NotBlank(message = "target word cannot be blank") String targetWord, int selectedWordId) {
}
