package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateSourceWordsRequest(List<String> sourceWords, @NotBlank String targetWord) {

    @Override
    public List<String> sourceWords() {
        return List.copyOf(sourceWords);
    }
}
