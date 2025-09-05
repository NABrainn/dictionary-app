package lule.dictionary.controllerAdvice;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
import lule.dictionary.language.service.LanguageData;
import lule.dictionary.localization.service.LocalizationService;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.language.service.Language;
import lule.dictionary.language.service.LanguageHelper;
import lule.dictionary.session.service.SessionHelper;
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

    private final TranslationService translationService;
    private final LanguageHelper languageHelper;
    private final SessionHelper sessionHelper;
    private final LocalizationService localizationService;


    @ModelAttribute
    public void addBaseAttribute(Model model,
                                 Authentication authentication,
                                 CsrfToken csrfToken) {
        if(authentication != null) {
            UserProfile principal = (UserProfile) authentication.getPrincipal();
            model.addAttribute("baseAttribute", BaseAttribute.builder()
                    ._csrf(csrfToken)
                    .isAuthenticated(authentication.isAuthenticated())
                    .username(principal.getUsername())
                    .sourceLanguageData(LanguageData.builder()
                            .language(principal.sourceLanguage())
                            .fullName(languageHelper.getFullName(principal.sourceLanguage()))
                            .languageCode(languageHelper.getCode(principal.sourceLanguage()))
                            .imgPath(languageHelper.getImagePath(principal.sourceLanguage()))
                            .build())
                    .targetLanguageData(LanguageData.builder()
                            .language(principal.targetLanguage())
                            .fullName(languageHelper.getFullName(principal.targetLanguage()))
                            .languageCode(languageHelper.getCode(principal.targetLanguage()))
                            .imgPath(languageHelper.getImagePath(principal.targetLanguage()))
                            .build())
                    .build());
            return;
        }
        model.addAttribute("baseAttribute", BaseAttribute.builder()
                ._csrf(csrfToken)
                .isAuthenticated(false)
                .username("defaultUser")
                .sourceLanguageData(LanguageData.builder()
                        .language(Language.EN)
                        .fullName(languageHelper.getFullName(Language.EN))
                        .languageCode(languageHelper.getCode(Language.EN))
                        .imgPath(languageHelper.getImagePath(Language.EN))
                        .build())
                .targetLanguageData(LanguageData.builder()
                        .language(Language.NO)
                        .fullName(languageHelper.getFullName(Language.NO))
                        .languageCode(languageHelper.getCode(Language.NO))
                        .imgPath(languageHelper.getImagePath(Language.NO))
                        .build())
                .build());
    }
    @ModelAttribute
    public void addNavbarAttribute(Model model,
                                   Authentication authentication,
                                   HttpSession httpSession) {
        if(authentication != null) {
            UserProfile principal = (UserProfile) authentication.getPrincipal();
            model.addAttribute("navbarAttribute", NavbarAttribute.builder()
                    .wordsLearned(translationService.getWordsLearnedCount(principal))
                    .wordsLearnedText(localizationService.navbarLocalization(principal.userInterfaceLanguage()).get("words"))
                    .daysSingularText(localizationService.navbarLocalization(principal.userInterfaceLanguage()).get("days_singular"))
                    .daysPluralText(localizationService.navbarLocalization(principal.userInterfaceLanguage()).get("days_plural"))
                    .loginBtnText(localizationService.navbarLocalization(principal.userInterfaceLanguage()).get("log_in"))
                    .lessonsBtnText(localizationService.navbarLocalization(principal.userInterfaceLanguage()).get("lessons"))
                    .vocabularyBtnText(localizationService.navbarLocalization(principal.userInterfaceLanguage()).get("vocabulary"))
                    .dailyStreak(principal.dailyStreak())
                    .languageDataList(languageHelper.getAllLanguageData())
                    .targetLanguage(LanguageData.builder()
                            .language(principal.targetLanguage())
                            .fullName(languageHelper.getFullName(principal.targetLanguage()))
                            .languageCode(languageHelper.getCode(principal.targetLanguage()))
                            .imgPath(languageHelper.getImagePath(principal.targetLanguage()))
                            .build())
                    .translationLanguage(LanguageData.builder()
                            .language(principal.sourceLanguage())
                            .fullName(languageHelper.getFullName(principal.sourceLanguage()))
                            .languageCode(languageHelper.getCode(principal.sourceLanguage()))
                            .imgPath(languageHelper.getImagePath(principal.sourceLanguage()))
                            .build())
                    .userInterfaceLanguage(LanguageData.builder()
                            .language(principal.userInterfaceLanguage())
                            .fullName(languageHelper.getFullName(principal.userInterfaceLanguage()))
                            .languageCode(languageHelper.getCode(principal.userInterfaceLanguage()))
                            .imgPath(languageHelper.getImagePath(principal.userInterfaceLanguage()))
                            .build())
                    .isProfileOpen(sessionHelper.getOrFalse(httpSession, "isProfileOpen"))
                    .settingsText(localizationService.settingsLocalization(principal.userInterfaceLanguage()).get("settings"))
                    .languageText(localizationService.settingsLocalization(principal.userInterfaceLanguage()).get("language"))
                    .uiText(localizationService.settingsLocalization(principal.userInterfaceLanguage()).get("user_interface"))
                    .translationsText(localizationService.settingsLocalization(principal.userInterfaceLanguage()).get("translations"))
                    .logoutText(localizationService.settingsLocalization(principal.userInterfaceLanguage()).get("log_out"))
                    .build());
            return;
        }
        Language sourceLanguage = sessionHelper.getSystemLanguageInfo(httpSession);
        model.addAttribute("navbarAttribute", NavbarAttribute.builder()
                .loginBtnText(localizationService.navbarLocalization(sourceLanguage).get("log_in"))
                .lessonsBtnText(localizationService.navbarLocalization(sourceLanguage).get("lessons"))
                .vocabularyBtnText(localizationService.navbarLocalization(sourceLanguage).get("vocabulary"))
                .wordsLearned(0)
                .dailyStreak(0)
                .isProfileOpen(false)
                .build());
    }
}
