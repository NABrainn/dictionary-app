package lule.dictionary.controllerAdvice.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.controllerAdvice.data.NavbarLocalizationKey;
import lule.dictionary.controllerAdvice.data.navbar.LanguageOption;
import lule.dictionary.language.service.Language;
import lule.dictionary.language.service.LanguageService;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.userProfiles.service.NavService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JteService {

    private final TranslationService translationService;
    private final NavService navService;
    private final LanguageService languageService;
    private final NavbarLocalizationService navbarLocalization;

    public int getWordsLearnedCount(UserProfile principal) {
        return translationService.getWordsLearnedCount(principal);
    }

    public Map<NavbarLocalizationKey, String> localization(Language uiLanguage) {
        return navbarLocalization.get(uiLanguage);
    }

    public List<LanguageOption> languageOptions() {
        return languageService.languageOptions();
    }

    public boolean isOpen(String progressionPanel, @NonNull String username) {
        return navService.isOpen("progressionPanel", username);
    }
}
