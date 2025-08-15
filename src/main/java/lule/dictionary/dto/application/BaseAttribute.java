package lule.dictionary.dto.application;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.security.web.csrf.CsrfToken;

@Builder
public record BaseAttribute(CsrfToken _csrf,
                            boolean isAuthenticated,
                            @NonNull String username,
                            @NonNull LanguageData sourceLanguageData,
                            @NonNull LanguageData targetLanguageData) {
}
