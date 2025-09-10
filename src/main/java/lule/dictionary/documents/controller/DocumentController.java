package lule.dictionary.documents.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.request.CreateDocumentRequest;
import lule.dictionary.documents.data.request.DocumentAttribute;
import lule.dictionary.documents.data.request.LoadDocumentContentRequest;
import lule.dictionary.documents.data.documentSubmission.ContentSubmissionStrategy;
import lule.dictionary.documents.data.attribute.DocumentFormAttribute;
import lule.dictionary.documents.data.documentSubmission.UrlSubmissionStrategy;
import lule.dictionary.errorLocalization.service.ErrorLocalization;
import lule.dictionary.documents.data.documentSubmission.SubmissionStrategy;
import lule.dictionary.documents.service.exception.DocumentNotFoundException;
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
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final SessionHelper sessionHelper;
    private final ErrorLocalization errorLocalization;


    @GetMapping({"", "/"})
    public String DocumentListPage() {
        return "redirect:/lessons";
    }

    @GetMapping({"/new", "/new/"})
    public String createDocumentForm(@RequestParam(name = "strategy", defaultValue = "url_submit") String strategy,
                                     Model model,
                                     Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        Map<DocumentLocalizationKey, String> localization = documentService.getDocumentFormLocalization(language);
        SubmissionStrategy submissionStrategy = strategy.equals("url_submit") ?
                        UrlSubmissionStrategy.of("", "", localization.get(DocumentLocalizationKey.SPACE_FOR_URL)) :
                        ContentSubmissionStrategy.of("", "", localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
        model.addAttribute("messages", Map.of());
        model.addAttribute("attribute", DocumentFormAttribute.of(submissionStrategy, localization));
        return "create-documentContentData-form/base-form";
    }

    @GetMapping({"/{documentId}", "/{documentId}/"})
    public String documentPage(@PathVariable("documentId") int documentId,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               Model model,
                               HttpSession session) {
        try {
            DocumentAttribute documentAttribute = documentService.loadDocumentContent(LoadDocumentContentRequest.of(0, documentId, page));
            model.addAttribute("documentAttribute", documentAttribute);
            model.addAttribute("isProfileOpen", sessionHelper.getOrFalse(session, "isProfileOpen"));
            return "documentContentData-page/base-page";

        }
        catch (InvalidUrlException e) {
            log.warn("Invalid url: {}", e.getMessage());
            return "error";
        }
        catch (DocumentNotFoundException e) {
            log.warn("Document not found: {}", e.getMessage());
            return "error";
        }
    }

    @GetMapping({"/{documentId}/reload", "/{documentId}/reload/"})
    public String reloadDocumentPage(@PathVariable("documentId") int documentId,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               Model model,
                               HttpSession session) {
        try {
            DocumentAttribute documentAttribute = documentService.loadDocumentContent(LoadDocumentContentRequest.of(0, documentId, page));
            model.addAttribute("documentAttribute", documentAttribute);
            model.addAttribute("isProfileOpen", sessionHelper.getOrFalse(session, "isProfileOpen"));
            return "documentContentData-page/content/content";

        }
        catch (InvalidUrlException e) {
            log.warn("Invalid url: {}", e.getMessage());
            return "error";
        }
        catch (DocumentNotFoundException e) {
            log.warn("Import not found: {}", e.getMessage());
            return "error";
        }
    }

    @PostMapping({"/new", "/new/"})
    public String createDocument(@RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 @RequestParam("url") String url,
                                 @RequestParam("strategy") String strategy,
                                 Model model,
                                 Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        Map<DocumentLocalizationKey, String> localization = documentService.getDocumentFormLocalization(language);
        SubmissionStrategy submissionStrategy = strategy.equals("url_submit") ?
                UrlSubmissionStrategy.of(title, url, localization.get(DocumentLocalizationKey.SPACE_FOR_URL)) :
                ContentSubmissionStrategy.of(title, content, localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
        try {
            int id = documentService.createDocument(CreateDocumentRequest.of(submissionStrategy, principal));
            return "redirect:/documents/" + id + "?page=1";

        } catch (ValidationServiceException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            model.addAttribute("messages", errorLocalization.getMessageByViolation(e.getViolation(), principal.userInterfaceLanguage()));
            model.addAttribute("attribute", DocumentFormAttribute.of(submissionStrategy, localization));
            return "create-document-form/base-form";
        } catch (InvalidUriException e) {
            log.warn("Retrying view due to url issue: {}", e.getMessage());
            model.addAttribute("messages", Map.of("invalidUriMessage", e.getLocalizedMessages().get(principal.userInterfaceLanguage())));
            model.addAttribute("attribute", DocumentFormAttribute.of(submissionStrategy, localization));
            return "create-document-form/base-form";
        }
    }

    @GetMapping({"/url-form", "/url-form/"})
    public String urlForm(Model model,
                          Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        Map<DocumentLocalizationKey, String> localization = documentService.getDocumentFormLocalization(language);
        model.addAttribute("spaceForUrlText", localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
        return "create-document-form/url-form";
    }

    @GetMapping({"/textarea-form", "/textarea-form/"})
    public String contentForm(Model model,
                              Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language language = principal.userInterfaceLanguage();
        Map<DocumentLocalizationKey, String> localization = documentService.getDocumentFormLocalization(language);
        model.addAttribute("spaceForContentText", localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
        return "create-document-form/content-form";
    }
}
