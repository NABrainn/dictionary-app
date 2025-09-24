package lule.dictionary.controllerAdvice.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.LanguageData;

@Builder
public record BaseAttribute(String csrf,
                            boolean isAuthenticated,
                            @NonNull String username,
                            @NonNull LanguageData sourceLanguageData,
                            @NonNull LanguageData targetLanguageData) {
}
