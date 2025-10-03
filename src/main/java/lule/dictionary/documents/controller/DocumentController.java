package lule.dictionary.documents.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.attribute.*;
import lule.dictionary.documents.data.exception.DocumentServiceException;
import lule.dictionary.documents.data.request.CreateDocumentRequest;
import lule.dictionary.documents.data.request.loadDocument.*;
import lule.dictionary.documents.data.response.LoadDocumentResponse;
import lule.dictionary.documents.data.response.ReadDocumentResponse;
import lule.dictionary.documents.data.response.ReloadWithPhraseResponse;
import lule.dictionary.documents.data.response.ReloadWithWordResponse;
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
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               @RequestParam(name = "selection", defaultValue = "none") String selection,
                               @RequestParam(name = "startId", required = false, defaultValue = "-1") int startId,
                               @RequestParam(name = "endId", required = false, defaultValue = "-1") int endId,
                               @RequestParam(name = "text", required = false, defaultValue = "") String text,
                               @RequestParam(name = "isPersisted", required = false, defaultValue = "false") boolean isPersisted,
                               Model model,
                               Authentication authentication) {
        DocumentInfo documentInfo = DocumentInfo.of(documentId, page);
        ReadDocumentRequest request = switch (selection) {
            case "word" -> {
                SelectedWordInfo selectedUnitInfo = SelectedWordInfo.of(text, isPersisted, startId);
                yield ReloadWithWord.of(documentInfo, selectedUnitInfo);
            }
            case "phrase" -> {
                SelectedPhraseInfo selectedUnitInfo = SelectedPhraseInfo.of(text, isPersisted, startId, endId);
                yield ReloadWithPhrase.of(documentInfo, selectedUnitInfo);
            }
            case "none" -> LoadDocumentRequest.of(documentInfo);
            default -> throw new IllegalStateException();
        };
        Result<ReadDocumentResponse> result = documentService.loadDocumentContent(request, authentication);
        return switch (result) {
            case Ok<ReadDocumentResponse> v -> switch (v.value()) {
                case LoadDocumentResponse loadDocumentResponse -> {
                    model.addAttribute("response", loadDocumentResponse);
                    yield "document/load/base-with-content";
                }
                case ReloadWithPhraseResponse reloadWithPhraseResponse -> {
                    model.addAttribute("response", reloadWithPhraseResponse);
                    yield  "document/reload/units-with-phrase";
                }
                case ReloadWithWordResponse reloadWithWordResponse -> {
                    model.addAttribute("response", reloadWithWordResponse);
                    yield  "document/reload/units-with-word";
                }
            };
            case Err<ReadDocumentResponse> ignored -> {
                log.warn("Failed to load document", ignored.throwable());
                yield  "error";
            }
        };
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
