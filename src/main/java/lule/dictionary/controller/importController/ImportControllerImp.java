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
    public String importListPage() {
        return "redirect:/lessons";
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
        return "create-import-form/base-form";
    }

    @GetMapping({"/{documentId}", "/{documentId}/"})
    public String importPage(@PathVariable("documentId") int importId,
                             @RequestParam(name = "page", defaultValue = "1") int page,
                             Model model,
                             Authentication authentication) {
        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            DocumentAttribute documentAttribute = loadDocumentContent(LoadDocumentContentRequest.of(0, importId, page));
            model.addAttribute("documentAttribute", documentAttribute);
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

    private DocumentAttribute loadDocumentContent(LoadDocumentContentRequest loadRequest) {
        return importService.loadDocumentContent(loadRequest).value();
    }
}
