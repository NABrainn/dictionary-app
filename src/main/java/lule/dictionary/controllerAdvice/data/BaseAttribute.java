package lule.dictionary.controllerAdvice.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.LanguageData;
import org.springframework.security.web.csrf.CsrfToken;

@Builder
public record BaseAttribute(CsrfToken _csrf,
                            boolean isAuthenticated,
                            @NonNull String username,
                            @NonNull LanguageData sourceLanguageData,
                            @NonNull LanguageData targetLanguageData) {
}
