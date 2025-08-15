package lule.dictionary.controller.lesson;

import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.attribute.DocumentsLocalizationAttribute;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.imports.importService.ImportServiceImp;
import lule.dictionary.service.imports.importService.dto.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.service.localization.LocalizationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LessonController {

    private final ImportServiceImp importService;
    private final LocalizationService localizationService;

    @GetMapping({"/lessons", "/lessons/"})
    public String lessonsPage(Model model,
                              Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        List<ImportWithTranslationData> imports = getImports(principal);
        model.addAttribute("documentsAttribute", imports);
        model.addAttribute("documentsLocalizationAttribute", DocumentsLocalizationAttribute.builder()
                .noDocumentsText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("there_are_no_documents_in_the_library"))
                .clickHereBtnText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("click_here"))
                .addFirstText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("to_add_your_first"))
                .wordsTotalText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("words_total"))
                .newWordsText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("new_words"))
                .translationsText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("translations"))
                .authorText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("author"))
                .addBookBtnText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("add_book"))
                .toAddYourFirstBtnText(localizationService.documentListLocalization(principal.userInterfaceLanguage()).get("to_add_your_first"))
                .build());
        return "document-list-page/documents";
    }
    private List<ImportWithTranslationData> getImports(CustomUserDetails principal) {
        return importService.findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest.of(principal.getUsername(), principal.targetLanguage())).value();
    }
}
