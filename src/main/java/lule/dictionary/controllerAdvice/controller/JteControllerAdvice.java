package lule.dictionary.controllerAdvice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.controllerAdvice.data.navbar.NavbarWithClosedPanel;
import lule.dictionary.controllerAdvice.data.navbar.NavbarWithOpenPanel;
import lule.dictionary.controllerAdvice.data.user.*;
import lule.dictionary.controllerAdvice.service.JteService;
import lule.dictionary.language.service.Language;
import lule.dictionary.userProfiles.data.UserProfile;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class JteControllerAdvice {

    private final JteService jteService;

    @ModelAttribute
    public void userAttribute(Model model,
                              Authentication authentication,
                              CsrfToken csrfToken) {
        var csrfTokenValue = csrfToken.getToken();

        switch (authentication) {
            case Authentication _ -> {
                var principal = (UserProfile) authentication.getPrincipal();

                var username = principal.username();

                var uiLanguage = principal.userInterfaceLanguage();
                var sourceLanguage = principal.sourceLanguage();
                var targetLanguage = principal.targetLanguage();
                var languageSettings = LanguageSettings.of(uiLanguage, sourceLanguage, targetLanguage);

                var wordsLearnedCount = jteService.getWordsLearnedCount(principal);
                var dailyStreakCount = principal.dailyStreak();

                var languageProgression = LanguageProgression.of(wordsLearnedCount, dailyStreakCount);
                var authenticatedUser = AuthenticatedUser.of(csrfTokenValue, username, languageSettings, languageProgression);
                model.addAttribute("user", authenticatedUser);
            }
            case null -> {
                var uiLanguage = Language.EN;
                var sourceLanguage = Language.EN;
                var targetLanguage = Language.NO;
                var languageSettings = LanguageSettings.of(uiLanguage, sourceLanguage, targetLanguage);

                var anonymousUser = AnonymousUser.of(csrfTokenValue, languageSettings);
                model.addAttribute("user", anonymousUser);
            }
        }
    }
    @ModelAttribute
    public void navbarAttribute(Model model,
                                User user) {
        switch (user) {
            case AuthenticatedUser authenticatedUser -> {
                var uiLanguage = authenticatedUser.languageSettings().uiLanguage();
                var localization = jteService.localization(uiLanguage);

                var languageOptions = jteService.languageOptions();
                var navbar = jteService.isOpen("progressionPanel", authenticatedUser.username()) ?
                        NavbarWithOpenPanel.of(localization, authenticatedUser, languageOptions) :
                        NavbarWithClosedPanel.of(localization, authenticatedUser, languageOptions);
                model.addAttribute("navbar", navbar);
            }
            case AnonymousUser anonymousUser -> {
                var localization = jteService.localization(Language.EN);

                var languageOptions = jteService.languageOptions();
                var navbar = NavbarWithClosedPanel.of(localization, anonymousUser, languageOptions);
                model.addAttribute("navbar", navbar);
            }
        }
    }
}
