package lule.dictionary.controller.userSettings;

import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.LanguageData;
import lule.dictionary.dto.application.attribute.NavbarAttribute;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.sessionHelper.SessionHelper;
import lule.dictionary.service.translation.TranslationService;
import lule.dictionary.service.translation.dto.request.GetWordsLearnedCountRequest;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final UserProfileService userProfileService;
    private final LanguageHelper languageHelper;
    private final TranslationService translationService;
    private final LocalizationService localizationService;
    private final SessionHelper sessionHelper;

    @GetMapping({"/changeLanguage", "/changeLanguage/"})
    public String changeLanguage(@RequestParam("lang") String language,
                                 Authentication authentication) {
        String username = extractUsername(authentication);
        updateTargetLanguage(username, language);
        return "redirect:/";
    }

    @GetMapping({"/profilePanel", "/profilePanel/"})
    public String profilePanel(Model model,
                               Authentication authentication,
                               HttpSession httpSession) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        boolean isProfileOpen = sessionHelper.getOrDefault(httpSession, "isProfileOpen", false);
        httpSession.setAttribute("isProfileOpen", !isProfileOpen);
        model.addAttribute("isProfileOpen", !isProfileOpen);
        model.addAttribute("navbarAttribute", NavbarAttribute.builder()
                .languageDataList(languageHelper.getAllLanguageData())
                .targetLanguage(LanguageData.of(
                                principal.targetLanguage(),
                                languageHelper.getFullName(principal.targetLanguage()),
                                languageHelper.getAbbreviation(principal.targetLanguage()),
                                languageHelper.getImagePath(principal.targetLanguage())
                        )
                )
                .wordsLearned(translationService.getWordsLearnedCount(principal).value())
                .dailyStreak(principal.dailyStreak())
                .wordsLearnedText(localizationService.navbarLocalization(principal.sourceLanguage()).get("words"))
                .daysSingularText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_singular"))
                .daysPluralText(localizationService.navbarLocalization(principal.sourceLanguage()).get("days_plural"))
                .logoutBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("log_out"))
                .loginBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("log_in"))
                .homeBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("home"))
                .build());
        return "navbar/profile-panel";
    }

    private void updateTargetLanguage(String username, String language) {
        userProfileService.updateTargetLanguage(username, Language.valueOf(language));
    }

    private String extractUsername(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
