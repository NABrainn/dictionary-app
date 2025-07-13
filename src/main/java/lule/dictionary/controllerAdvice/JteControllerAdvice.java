package lule.dictionary.controllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.BaseAttribute;
import lule.dictionary.dto.application.LanguageData;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
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

    @ModelAttribute
    public void addBaseAttribute(Model model, Authentication authentication, CsrfToken csrfToken) {
        if(authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("baseAttribute", BaseAttribute.builder()
                    ._csrf(csrfToken)
                    .isAuthenticated(authentication.isAuthenticated())
                    .username(userDetails.getUsername())
                    .sourceLanguageData(LanguageData.of(
                                    userDetails.sourceLanguage(),
                                    languageHelper.getFullName(userDetails.sourceLanguage()),
                                    languageHelper.getAbbreviation(userDetails.sourceLanguage())
                            )
                    )
                    .targetLanguageData(LanguageData.of(
                                    userDetails.targetLanguage(),
                                    languageHelper.getFullName(userDetails.targetLanguage()),
                                    languageHelper.getAbbreviation(userDetails.targetLanguage())
                            )
                    )
                    .allLanguageData(languageHelper.getAllLanguageData())
                    .wordsLearned(translationService.getWordsLearnedCount(userDetails.getUsername(), userDetails.targetLanguage()).value())
                    .dailyStreak(userDetails.dailyStreak())
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
                                languageHelper.getAbbreviation(Language.EN)
                        )
                )
                .targetLanguageData(LanguageData.of(
                                Language.NO,
                                languageHelper.getFullName(Language.NO),
                                languageHelper.getAbbreviation(Language.NO)
                        )
                )
                .allLanguageData(languageHelper.getAllLanguageData())
                .wordsLearned(0)
                .dailyStreak(0)
                .build());
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null;
    }
}
