package lule.dictionary.dto.application;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.List;

@Builder
public record BaseAttribute(CsrfToken _csrf,
                            boolean isAuthenticated,
                            @NonNull String username,
                            @NonNull LanguageData sourceLanguageData,
                            @NonNull LanguageData targetLanguageData,
                            @NonNull List<LanguageData> allLanguageData,
                            int wordsLearned,
                            int dailyStreak) {
    @Override
    public List<LanguageData> allLanguageData() {
        return List.copyOf(allLanguageData);
    }
}
