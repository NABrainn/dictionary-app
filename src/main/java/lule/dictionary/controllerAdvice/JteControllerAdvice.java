package lule.dictionary.controllerAdvice;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.BaseAttribute;
import lule.dictionary.dto.application.LanguageData;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.sessionHelper.SessionHelper;
import lule.dictionary.service.translation.TranslationService;
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


    @ModelAttribute
    public void addBaseAttribute(Model model,
                                 Authentication authentication,
                                 CsrfToken csrfToken,
                                 HttpSession httpSession) {
        if(authentication != null) {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("baseAttribute", BaseAttribute.builder()
                    ._csrf(csrfToken)
                    .isAuthenticated(authentication.isAuthenticated())
                    .username(principal.getUsername())
                    .sourceLanguageData(LanguageData.of(
                                    principal.sourceLanguage(),
                                    languageHelper.getFullName(principal.sourceLanguage()),
                                    languageHelper.getCode(principal.sourceLanguage()),
                                    languageHelper.getImagePath(principal.sourceLanguage())
                            )
                    )
                    .targetLanguageData(LanguageData.of(
                                    principal.targetLanguage(),
                                    languageHelper.getFullName(principal.targetLanguage()),
                                    languageHelper.getCode(principal.targetLanguage()),
                                    languageHelper.getImagePath(principal.targetLanguage())
                            )
                    )
                    .allLanguageData(languageHelper.getAllLanguageData())
                    .wordsLearned(translationService.getWordsLearnedCount(principal).value()
                    )
                    .dailyStreak(principal.dailyStreak())
                    .isProfileOpen(sessionHelper.getOrDefault(httpSession, "isProfileOpen", false))
                    .build());
            return;
        }
        model.addAttribute("baseAttribute", BaseAttribute.builder()
                ._csrf(csrfToken)
                .isAuthenticated(false)
                .username("defaultUser")
                .sourceLanguageData(LanguageData.of(
                                Language.EN,
                                languageHelper.getFullName(Language.EN),
                                languageHelper.getCode(Language.EN),
                                languageHelper.getImagePath(Language.EN)
                        )
                )
                .targetLanguageData(LanguageData.of(
                                Language.NO,
                                languageHelper.getFullName(Language.NO),
                                languageHelper.getCode(Language.NO),
                                languageHelper.getImagePath(Language.NO)
                        )
                )
                .allLanguageData(languageHelper.getAllLanguageData())
                .wordsLearned(0)
                .dailyStreak(0)
                .isProfileOpen(false)
                .build());
    }
}
