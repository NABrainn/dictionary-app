package lule.dictionary.controllerAdvice;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
import lule.dictionary.controllerAdvice.data.NavbarLocalizationKey;
import lule.dictionary.controllerAdvice.service.NavbarLocalizationService;
import lule.dictionary.language.service.LanguageData;
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
    private final NavbarLocalizationService navbarLocalization;

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
            Language sourceLanguage = principal.userInterfaceLanguage();
            model.addAttribute("navbarAttribute", NavbarAttribute.builder()
                    .wordsLearned(translationService.getWordsLearnedCount(principal))
                    .wordsLearnedText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.WORDS))
                    .daysSingularText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.DAYS_SINGULAR))
                    .daysPluralText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.DAYS_PLURAL))
                    .loginBtnText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.LOG_IN))
                    .lessonsBtnText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.LESSONS))
                    .vocabularyBtnText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.VOCABULARY))
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
                            .language(sourceLanguage)
                            .fullName(languageHelper.getFullName(sourceLanguage))
                            .languageCode(languageHelper.getCode(sourceLanguage))
                            .imgPath(languageHelper.getImagePath(sourceLanguage))
                            .build())
                    .isProfileOpen(sessionHelper.getOrFalse(httpSession, "isProfileOpen"))
                    .settingsText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.SETTINGS))
                    .languageText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.LANGUAGE))
                    .uiText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.USER_INTERFACE))
                    .translationsText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.TRANSLATIONS))
                    .logoutText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.LOG_OUT))
                    .build());
            return;
        }
        Language sourceLanguage = sessionHelper.getSystemLanguageInfo(httpSession);
        model.addAttribute("navbarAttribute", NavbarAttribute.builder()
                .loginBtnText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.LOG_IN))
                .lessonsBtnText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.LESSONS))
                .vocabularyBtnText(navbarLocalization.get(sourceLanguage).get(NavbarLocalizationKey.VOCABULARY))
                .wordsLearned(0)
                .dailyStreak(0)
                .isProfileOpen(false)
                .build());
    }
}
