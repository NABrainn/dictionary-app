package lule.dictionary.controller.vocabularyController;

import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.LanguageData;
import lule.dictionary.dto.application.attribute.NavbarAttribute;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.translation.TranslationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/vocabulary", "/vocabulary/"})
public class VocabularyController {

    private final LanguageHelper languageHelper;
    private final LocalizationService localizationService;
    private final TranslationService translationService;

    @GetMapping({"", "/"})
    public String vocabularyPage(Model model,
                                 Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("navbarAttribute", NavbarAttribute.builder()
                .languageDataList(languageHelper.getAllLanguageData())
                .targetLanguage(LanguageData.of(
                                principal.targetLanguage(),
                                languageHelper.getFullName(principal.targetLanguage()),
                                languageHelper.getCode(principal.targetLanguage()),
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
                .lessonsBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("lessons"))
                .vocabularyBtnText(localizationService.navbarLocalization(principal.sourceLanguage()).get("vocabulary"))
                .build());
        return "vocabulary-page/base-page";
    }
}
