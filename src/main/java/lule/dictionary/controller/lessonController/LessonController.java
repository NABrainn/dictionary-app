package lule.dictionary.controller.lessonController;

import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.LanguageData;
import lule.dictionary.dto.application.attribute.DocumentsLocalizationAttribute;
import lule.dictionary.dto.application.attribute.NavbarAttribute;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.imports.importService.ImportServiceImp;
import lule.dictionary.service.imports.importService.dto.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.translation.TranslationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LessonController {

    private final ImportServiceImp importService;
    private final TranslationService translationService;
    private final LocalizationService localizationService;
    private final LanguageHelper languageHelper;

    @GetMapping({"/lessons", "/lessons/"})
    public String lessonsPage(Model model,
                              Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        List<ImportWithTranslationData> imports = getImports(principal);
        model.addAttribute("documentsAttribute", imports);
        model.addAttribute("documentsLocalizationAttribute", DocumentsLocalizationAttribute.builder()
                .noDocumentsText(localizationService.documentListLocalization(principal.sourceLanguage()).get("there_are_no_documents_in_the_library"))
                .clickHereBtnText(localizationService.documentListLocalization(principal.sourceLanguage()).get("click_here"))
                .addFirstText(localizationService.documentListLocalization(principal.sourceLanguage()).get("to_add_your_first"))
                .wordsTotalText(localizationService.documentListLocalization(principal.sourceLanguage()).get("words_total"))
                .newWordsText(localizationService.documentListLocalization(principal.sourceLanguage()).get("new_words"))
                .translationsText(localizationService.documentListLocalization(principal.sourceLanguage()).get("translations"))
                .authorText(localizationService.documentListLocalization(principal.sourceLanguage()).get("author"))
                .addBookBtnText(localizationService.documentListLocalization(principal.sourceLanguage()).get("add_book"))
                .build());
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
        return "document-list-page/documents";
    }
    private List<ImportWithTranslationData> getImports(CustomUserDetails principal) {
        return importService.findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest.of(principal.getUsername(), principal.targetLanguage())).value();
    }
}
