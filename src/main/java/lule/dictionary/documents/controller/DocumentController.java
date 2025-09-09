package lule.dictionary.documents.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.request.CreateDocumentRequest;
import lule.dictionary.documents.data.request.DocumentAttribute;
import lule.dictionary.documents.data.request.LoadDocumentContentRequest;
import lule.dictionary.documents.data.documentSubmission.ContentSubmissionStrategy;
import lule.dictionary.documents.data.DocumentFormAttribute;
import lule.dictionary.documents.data.documentSubmission.UrlSubmissionStrategy;
import lule.dictionary.errorLocalization.service.ErrorLocalization;
import lule.dictionary.localization.service.LocalizationService;
import lule.dictionary.documents.data.documentSubmission.SubmissionStrategy;
import lule.dictionary.documents.service.exception.ImportNotFoundException;
import lule.dictionary.documents.service.DocumentService;
import lule.dictionary.jsoup.service.exception.InvalidUriException;
import lule.dictionary.language.service.Language;
import lule.dictionary.session.service.SessionHelper;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.validation.service.ValidationServiceException;
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
public class DocumentController {

    private final DocumentService documentService;
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
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        SubmissionStrategy submissionStrategy = strategy.equals("url_submit") ?
                        UrlSubmissionStrategy.of("", "", localizationService.documentFormLocalization(language).get("space_for_url")) :
                        ContentSubmissionStrategy.of("", "", localizationService.documentFormLocalization(language).get("space_for_content"));
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
                             HttpSession session) {
        try {
            DocumentAttribute documentAttribute = documentService.loadDocumentContent(LoadDocumentContentRequest.of(0, importId, page));
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
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        SubmissionStrategy submissionStrategy = strategy.equals("url_submit") ?
                UrlSubmissionStrategy.of(title, url, localizationService.documentFormLocalization(language).get("space_for_url")) :
                ContentSubmissionStrategy.of(title, content, localizationService.documentFormLocalization(language).get("space_for_content"));
        try {
            int id = documentService.createDocument(CreateDocumentRequest.of(submissionStrategy, principal));
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
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        model.addAttribute("spaceForUrlText", localizationService.documentFormLocalization(language).get("space_for_url"));
        return "create-document-form/url-form";
    }

    @GetMapping({"/textarea-form", "/textarea-form/"})
    public String contentForm(Model model,
                              Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        model.addAttribute("spaceForContentText", localizationService.documentFormLocalization(language).get("space_for_content"));
        return "create-document-form/content-form";
    }
}
