package lule.dictionary.documents.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.attribute.DocumentListAttribute;
import lule.dictionary.documents.data.exception.DocumentServiceException;
import lule.dictionary.documents.data.request.CreateDocumentRequest;
import lule.dictionary.documents.data.request.DocumentAttribute;
import lule.dictionary.documents.data.request.LoadDocumentContentRequest;
import lule.dictionary.documents.data.attribute.DocumentFormAttribute;
import lule.dictionary.documents.service.exception.DocumentNotFoundException;
import lule.dictionary.documents.service.DocumentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.InvalidUrlException;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping({"/documents", "/lessons"})
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping({"", "/"})
    public String documentListPage(Authentication authentication,
                                   Model model) {
        DocumentListAttribute attribute = documentService.findMany(authentication);
        model.addAttribute("attribute", attribute);
        return "document/documents";
    }

    @GetMapping({"/{documentId}", "/{documentId}/"})
    public String documentPage(@PathVariable("documentId") int documentId,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               Model model,
                               HttpSession session) {
        try {
            DocumentAttribute documentAttribute = documentService.loadDocumentContent(LoadDocumentContentRequest.of(0, documentId, page, session));
            model.addAttribute("attribute", documentAttribute);
            model.addAttribute("isProfileOpen", false);
            return "document/base-page";
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
            DocumentAttribute documentAttribute = documentService.loadDocumentContent(LoadDocumentContentRequest.of(0, documentId, page, session));
            model.addAttribute("attribute", documentAttribute);
            model.addAttribute("isProfileOpen", false);
            return "document/content";
        }
        catch (InvalidUrlException | DocumentNotFoundException e) {
            log.warn("Invalid url: {}", e.getMessage());
            return "error";
        }
    }

    @GetMapping({"/new", "/new/"})
    public String createDocumentForm(@RequestParam(name = "strategy", defaultValue = "url_submit") String strategy,
                                     Model model,
                                     Authentication authentication) {
        DocumentFormAttribute attribute = documentService.getDocumentForm(strategy, authentication);
        model.addAttribute("violation", Map.of());
        model.addAttribute("attribute", attribute);
        return "document/base-form";
    }

    @PostMapping({"/new", "/new/"})
    public String createDocument(@RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 @RequestParam("url") String url,
                                 @RequestParam("strategy") String strategy,
                                 Model model,
                                 Authentication authentication) {
        try {
            int id = documentService.createDocument(CreateDocumentRequest.builder()
                    .submissionStrategy(strategy)
                    .authentication(authentication)
                    .title(title)
                    .content(content)
                    .url(url)
                    .build());
            return "redirect:/lessons/" + id + "?page=1";
        }
        catch (DocumentServiceException e) {
            model.addAttribute("violation", e.getViolation());
            model.addAttribute("attribute", e.getAttribute());
            return "document/base-form";
        }
    }

    @GetMapping({"/url-form", "/url-form/"})
    public String urlForm(Model model,
                          Authentication authentication) {
        Map<DocumentLocalizationKey, String> localization = documentService.getDocumentFormLocalization(authentication);
        model.addAttribute("urlPlaceholderText", localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
        return "document/url-form";
    }

    @GetMapping({"/textarea-form", "/textarea-form/"})
    public String contentForm(Model model,
                              Authentication authentication) {
        Map<DocumentLocalizationKey, String> localization = documentService.getDocumentFormLocalization(authentication);
        model.addAttribute("contentPlaceholderText", localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
        return "document/content-form";
    }
}
