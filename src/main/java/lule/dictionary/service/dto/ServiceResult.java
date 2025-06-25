package lule.dictionary.service.dto;

import lombok.NonNull;

import java.util.Map;

public record ServiceResult(boolean error, @NonNull Map<String, String> errors) {

    @Override
    public Map<String, String> errors() {
        return Map.copyOf(errors);
    }
}
