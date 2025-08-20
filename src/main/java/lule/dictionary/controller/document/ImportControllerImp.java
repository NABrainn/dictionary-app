package lule.dictionary.controller.document;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.service.imports.dto.ContentSubmission;
import lule.dictionary.service.imports.dto.DocumentFormAttribute;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.imports.dto.SubmissionStrategy;
import lule.dictionary.service.imports.dto.UrlSubmission;
import lule.dictionary.service.imports.exception.ImportNotFoundException;
import lule.dictionary.service.imports.importService.dto.request.*;
import lule.dictionary.service.imports.importService.ImportServiceImp;
import lule.dictionary.service.jsoup.exception.InvalidUriException;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.localization.ErrorLocalization;
import lule.dictionary.service.localization.LocalizationService;
import lule.dictionary.service.sessionHelper.SessionHelper;
import lule.dictionary.service.validation.ValidationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.InvalidUrlException;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/imports")
@RequiredArgsConstructor
public class ImportControllerImp implements ImportController {

    private final ImportServiceImp importService;
    private final LocalizationService localizationService;
    private final SessionHelper sessionHelper;
    private final ErrorLocalization errorLocalization;


    @GetMapping({"", "/"})
    public String importListPage() {
        return "redirect:/lessons";
    }

    @GetMapping({"/new", "/new/"})
    public String createImportForm(@RequestParam(name = "strategy", defaultValue = "url_submit") String strategy,
                                   Model model,
                                   Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        SubmissionStrategy submissionStrategy = strategy.equals("url_submit") ?
                        UrlSubmission.of("", "", localizationService.documentFormLocalization(language).get("space_for_url")) :
                        ContentSubmission.of("", "", localizationService.documentFormLocalization(language).get("space_for_content"));
        model.addAttribute("messages", Map.of());
        model.addAttribute("documentFormAttribute", DocumentFormAttribute.builder()
                .titleText(localizationService.documentFormLocalization(language).get("title"))
                .contentText(localizationService.documentFormLocalization(language).get("content"))
                .importByUrlBtnText(localizationService.documentFormLocalization(language).get("import_by_url"))
                .insertManuallyBtnText(localizationService.documentFormLocalization(language).get("insert_manually"))
                .submitBtnText(localizationService.documentFormLocalization(language).get("submit"))
                .submissionStrategy(submissionStrategy)
                .build());
        return "create-document-form/base-form";
    }

    @GetMapping({"/{documentId}", "/{documentId}/"})
    public String importPage(@PathVariable("documentId") int importId,
                             @RequestParam(name = "page", defaultValue = "1") int page,
                             Model model,
                             Authentication authentication,
                             HttpSession session) {
        try {
            DocumentAttribute documentAttribute = importService.loadDocumentContent(LoadDocumentContentRequest.of(0, importId, page));
            model.addAttribute("documentAttribute", documentAttribute);
            model.addAttribute("isProfileOpen", sessionHelper.getOrFalse(session, "isProfileOpen"));
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
                               @RequestParam("strategy") String strategy,
                               Model model,
                               Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        SubmissionStrategy submissionStrategy = strategy.equals("url_submit") ?
                UrlSubmission.of(title, url, localizationService.documentFormLocalization(language).get("space_for_url")) :
                ContentSubmission.of(title, content, localizationService.documentFormLocalization(language).get("space_for_content"));
        try {
            int id = importService.createImport(CreateDocumentRequest.of(submissionStrategy, principal));
            return "redirect:/imports/" + id + "?page=1";

        } catch (ValidationServiceException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            model.addAttribute("messages", errorLocalization.getMessageByViolation(e.getViolation(), principal.userInterfaceLanguage()));
            model.addAttribute("documentFormAttribute", DocumentFormAttribute.builder()
                    .titleText(localizationService.documentFormLocalization(language).get("title"))
                    .contentText(localizationService.documentFormLocalization(language).get("content"))
                    .importByUrlBtnText(localizationService.documentFormLocalization(language).get("import_by_url"))
                    .insertManuallyBtnText(localizationService.documentFormLocalization(language).get("insert_manually"))
                    .submitBtnText(localizationService.documentFormLocalization(language).get("submit"))
                    .submissionStrategy(submissionStrategy)
                    .build());
            return "create-document-form/base-form";
        } catch (InvalidUriException e) {
            log.warn("Retrying view due to url issue: {}", e.getMessage());
            model.addAttribute("messages", Map.of("invalidUriMessage", e.getLocalizedMessages().get(principal.userInterfaceLanguage())));
            model.addAttribute("documentFormAttribute", DocumentFormAttribute.builder()
                    .titleText(localizationService.documentFormLocalization(language).get("title"))
                    .contentText(localizationService.documentFormLocalization(language).get("content"))
                    .importByUrlBtnText(localizationService.documentFormLocalization(language).get("import_by_url"))
                    .insertManuallyBtnText(localizationService.documentFormLocalization(language).get("insert_manually"))
                    .submitBtnText(localizationService.documentFormLocalization(language).get("submit"))
                    .submissionStrategy(submissionStrategy)
                    .build());
            return "create-document-form/base-form";
        }
    }

    @GetMapping({"/url-form", "/url-form/"})
    public String urlForm(Model model,
                          Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        model.addAttribute("spaceForUrlText", localizationService.documentFormLocalization(language).get("space_for_url"));
        return "create-document-form/url-form";
    }

    @GetMapping({"/textarea-form", "/textarea-form/"})
    public String contentForm(Model model,
                              Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        model.addAttribute("spaceForContentText", localizationService.documentFormLocalization(language).get("space_for_content"));
        return "create-document-form/content-form";
    }
}
