package lule.dictionary.lessons.controller;

import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.DocumentWithTranslationData;
import lule.dictionary.userProfiles.data.CustomUserDetails;
import lule.dictionary.documents.service.DocumentServiceImp;
import lule.dictionary.documents.service.dto.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.localization.service.LocalizationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LessonController {

    private final DocumentServiceImp documentService;
    private final LocalizationService localizationService;

    @GetMapping({"/lessons", "/lessons/"})
    public String lessonsPage(Model model,
                              Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        List<DocumentWithTranslationData> imports = getImports(principal);
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
    private List<DocumentWithTranslationData> getImports(CustomUserDetails principal) {
        return documentService.findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest.of(principal.getUsername(), principal.targetLanguage()));
    }
}
