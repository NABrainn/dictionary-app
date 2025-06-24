package lule.dictionary.service.dto;

import lombok.NonNull;

import java.util.Map;

public record ServiceResult(boolean error, @NonNull Map<String, String> message) {

    @Override
    public Map<String, String> message() {
        return Map.copyOf(message);
    }
}
