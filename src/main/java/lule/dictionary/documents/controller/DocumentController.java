package lule.dictionary.documents.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.attribute.*;
import lule.dictionary.documents.data.exception.DocumentServiceException;
import lule.dictionary.documents.data.request.CreateDocumentRequest;
import lule.dictionary.documents.data.request.loadDocument.*;
import lule.dictionary.documents.data.result.PageChangeResult;
import lule.dictionary.documents.data.result.FirstLoadResult;
import lule.dictionary.documents.data.result.LoadDocumentResult;
import lule.dictionary.documents.service.DocumentService;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String loadDocument(@PathVariable("documentId") int documentId,
                               @RequestParam(name = "unitText", defaultValue = "") String unitText,
                               @RequestParam(name = "isUnitPersisted", defaultValue = "false") boolean isUnitPersisted,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               Model model,
                               Authentication authentication) {
        LoadDocumentRequest request = FirstLoadRequest.builder()
                .startId(-1)
                .documentId(documentId)
                .page(page)
                .unitText(unitText)
                .isUnitPersisted(isUnitPersisted)
                .length(-1)
                .build();
        Result<LoadDocumentResult> result = documentService.loadDocumentContent(request, authentication);
        switch (result) {
            case Ok<LoadDocumentResult> v -> {
                model.addAttribute("result", v.value());
                return switch (v.value()) {
                    case FirstLoadResult ignored -> "document/load/base-with-content";
                    case PageChangeResult ignored -> "document/load/content";
                    default -> throw new RuntimeException("Unexpected operation");
                };
            }
            case Err<LoadDocumentResult> ignored -> {
                log.warn("Failed to load document", ignored.throwable());
                return "error";
            }
        }
    }

    @GetMapping({"/{documentId}/changePage", "/{documentId}/changePage/"})
    public String changePageDocument(@PathVariable("documentId") int documentId,
                                     @RequestParam(name = "unitText", defaultValue = "") String unitText,
                                     @RequestParam(name = "isUnitPersisted", defaultValue = "false") boolean isUnitPersisted,
                                     @RequestParam(name = "page", defaultValue = "1") int page,
                                     Model model,
                                     Authentication authentication) {
        LoadDocumentRequest request = PageChangeRequest.builder()
                .startId(-1)
                .documentId(documentId)
                .page(page)
                .unitText(unitText)
                .isUnitPersisted(isUnitPersisted)
                .length(-1)
                .build();
        Result<LoadDocumentResult> result = documentService.loadDocumentContent(request, authentication);
        switch (result) {
            case Ok<LoadDocumentResult> v -> {
                model.addAttribute("result", v.value());
                return "document/load/content";
            }
            case Err<LoadDocumentResult> ignored -> {
                log.warn("Failed to load document", ignored.throwable());
                return "error";
            }
        }
    }

    @GetMapping({"/{documentId}/withSelectedWord", "/{documentId}/withSelectedWord/"})
    public String documentWithSelectedWord(@PathVariable("documentId") int documentId,
                                           @RequestParam(name = "startId", defaultValue = "-1") int startId,
                                           @RequestParam(name = "unitText", defaultValue = "") String unitText,
                                           @RequestParam(name = "isUnitPersisted", defaultValue = "false") boolean isUnitPersisted,
                                           @RequestParam(name = "page", defaultValue = "1") int page,
                                           Model model,
                                           Authentication authentication) {
        LoadDocumentRequest request = ReloadWithWordRequest.builder()
                .startId(startId)
                .documentId(documentId)
                .page(page)
                .unitText(unitText)
                .isUnitPersisted(isUnitPersisted)
                .length(1)
                .build();
        Result<LoadDocumentResult> result = documentService.loadDocumentContent(request, authentication);
        switch (result) {
            case Ok<LoadDocumentResult> v -> {
                model.addAttribute("result", v.value());
                return "document/reload/units-with-word";
            }
            case Err<LoadDocumentResult> ignored -> {
                log.warn("Failed to load document", ignored.throwable());
                return "error";
            }
        }
    }

    @GetMapping({"/{documentId}/withSelectedPhrase", "/{documentId}/withSelectedPhrase/"})
    public String documentWithSelectedPhrase(@PathVariable("documentId") int documentId,
                                             @RequestParam(name = "startId", defaultValue = "-1") int startId,
                                             @RequestParam(name = "length", defaultValue = "-1") int length,
                                             @RequestParam(name = "unitText", defaultValue = "") String unitText,
                                             @RequestParam(name = "isUnitPersisted", defaultValue = "false") boolean isUnitPersisted,
                                             @RequestParam(name = "page", defaultValue = "1") int page,
                                             Model model,
                                             Authentication authentication) {
        LoadDocumentRequest request = ReloadWithPhraseRequest.builder()
                .startId(startId)
                .length(length)
                .documentId(documentId)
                .page(page)
                .unitText(unitText)
                .isUnitPersisted(isUnitPersisted)
                .build();
        Result<LoadDocumentResult> result = documentService.loadDocumentContent(request, authentication);
        switch (result) {
            case Ok<LoadDocumentResult> v -> {
                model.addAttribute("result", v.value());
                return "document/reload/units-with-phrase";
            }
            case Err<LoadDocumentResult> ignored -> {
                log.warn("Failed to load document", ignored.throwable());
                return "error";
            }
        }
    }

    @GetMapping({"/new", "/new/"})
    public String createDocumentForm(@RequestParam(name = "form_type", defaultValue = "url_form") String formType,
                                     Model model,
                                     Authentication authentication) {
        DocumentFormAttribute attribute = documentService.getDocumentForm(formType, authentication);
        model.addAttribute("errors", Map.of());
        model.addAttribute("attribute", attribute);
        return "document/base-form";
    }

    @PostMapping({"/new", "/new/"})
    public String createDocument(@RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 @RequestParam("url") String url,
                                 @RequestParam("form_type") String formType,
                                 Model model,
                                 Authentication authentication) {
        Result<Integer> result = documentService.createDocument(CreateDocumentRequest.builder()
                .documentFormType(formType)
                .authentication(authentication)
                .title(title)
                .content(content)
                .url(url)
                .build());
        return switch (result) {
            case Ok<Integer> v -> "redirect:/lessons/" + v.value() + "?page=1";
            case Err<Integer> v -> {
                if (v.throwable() instanceof DocumentServiceException documentServiceException) {
                    model.addAttribute("errors", documentServiceException.getViolation());
                    model.addAttribute("attribute", documentServiceException.getAttribute());
                    yield "document/base-form";
                }
                yield "error";
            }
        };
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
