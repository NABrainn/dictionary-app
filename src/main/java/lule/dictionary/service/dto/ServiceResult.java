package lule.dictionary.service.dto;

import lombok.NonNull;

import java.util.Collections;
import java.util.Map;

public record ServiceResult(boolean error, @NonNull Map<String, String> messages) {

    @Override
    public Map<String, String> messages() {
        return Collections.unmodifiableMap(messages);
    }
}
