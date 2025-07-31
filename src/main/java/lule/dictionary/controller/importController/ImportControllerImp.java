package lule.dictionary.controller.importController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.LanguageData;
import lule.dictionary.dto.application.attribute.DocumentFormAttribute;
import lule.dictionary.dto.application.attribute.DocumentsLocalizationAttribute;
import lule.dictionary.dto.application.attribute.NavbarAttribute;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importService.dto.request.*;
import lule.dictionary.service.imports.importService.ImportServiceImp;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.translation.TranslationService;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.InvalidUrlException;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/imports")
@RequiredArgsConstructor
public class ImportControllerImp implements ImportController {

    private final ImportServiceImp importService;
    private final TranslationService translationService;
    private final LocalizationService localizationService;
    private final LanguageHelper languageHelper;

    @GetMapping({"", "/"})
    public String importListPage(Model model,
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
        return "document-list-page/documents";
    }

    @GetMapping({"/new", "/new/"})
    public String createImportForm(Model model,
                                   Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("documentFormAttribute", DocumentFormAttribute.builder()
                .titleText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("title"))
                .contentText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("content"))
                .importByUrlBtnText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("import_by_url"))
                .insertManuallyBtnText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("insert_manually"))
                .submitBtnText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("submit"))
                .spaceForUrlText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("space_for_url"))
                .spaceForContentText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("space_for_content"))
                .build());
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
        return "create-import-form/base-form";
    }

    @GetMapping({"/{importId}", "/{importId}/"})
    public String importPage(@PathVariable("importId") int importId,
                             @RequestParam(name = "page", defaultValue = "1") int page,
                             Model model,
                             Authentication authentication) {
        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            DocumentContentAttribute documentContentAttribute = loadDocumentContent(LoadDocumentContentRequest.of(0, importId, page));
            model.addAttribute("documentContentAttribute", documentContentAttribute);
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
            return "document-page/base-page";

        }
        catch (InvalidUrlException e) {
            log.warn("Invalid url: {}", e.getMessage());
            return "error";
        }
        catch (ImportNotFoundException e) {
            log.warn("Import not found: {}", e.getMessage());
            return "error";
        }
    }

    @PostMapping({"/new", "/new/"})
    public String createImport(@RequestParam("title") String title,
                               @RequestParam("content") String content,
                               @RequestParam("url") String url,
                                Model model,
                                Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        try {
            ServiceResult<Integer> result = importService.createImport(CreateImportRequest.of(title, content, url, extractUsername(authentication)));
            model.addAttribute("result", result);
            return "redirect:/imports/" + result.value() + "?page=1";
        } catch (UserNotFoundException e) {
            log.info("Redirecting to login page due to user not found: {}", e.getMessage());
            return "redirect:/auth/login";

        } catch (InvalidInputException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            model.addAttribute("result", e.getResult());
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
            model.addAttribute("documentFormAttribute", DocumentFormAttribute.builder()
                    .titleText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("title"))
                    .contentText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("content"))
                    .importByUrlBtnText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("import_by_url"))
                    .insertManuallyBtnText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("insert_manually"))
                    .submitBtnText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("submit"))
                    .spaceForUrlText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("space_for_url"))
                    .spaceForContentText(localizationService.documentFormLocalization(principal.sourceLanguage()).get("space_for_content"))
                    .build());
            return "create-import-form/base-form";
        }
    }

    @GetMapping({"/url-form", "/url-form/"})
    public String urlForm(Model model,
                          Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("spaceForUrlText", localizationService.documentFormLocalization(principal.sourceLanguage()).get("space_for_url"));
        return "create-import-form/url-form";
    }

    @GetMapping({"/textarea-form", "/textarea-form/"})
    public String contentForm(Model model,
                              Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("spaceForContentText", localizationService.documentFormLocalization(principal.sourceLanguage()).get("space_for_content"));
        return "create-import-form/content-form";
    }


    private @NonNull String extractUsername(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private DocumentContentAttribute loadDocumentContent(LoadDocumentContentRequest loadRequest) {
        return importService.loadDocumentContent(loadRequest).value();
    }

    private List<ImportWithTranslationData> getImports(CustomUserDetails principal) {
        return importService.findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest.of(principal.getUsername(), principal.targetLanguage())).value();
    }
}
