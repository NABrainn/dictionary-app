package lule.dictionary.controller.importController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.exception.RetryViewException;
import lule.dictionary.service.imports.importPageService.dto.LoadImportRequest;
import lule.dictionary.service.imports.importService.ImportService;
import lule.dictionary.service.imports.importPageService.ImportPageService;
import lule.dictionary.service.imports.importService.dto.AddImportRequest;
import lule.dictionary.service.translation.dto.TranslationModel;
import lule.dictionary.service.userProfile.UserProfileService;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.InvalidUrlException;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping({"/imports", "/imports/"})
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;
    private final ImportPageService importPageService;
    private final UserProfileService userProfileService;

    @GetMapping({"", "/"})
    public String getImportList(Authentication authentication,
                                Model model) {
        importService.findByOwner(model, authentication.getName());
        return "imports";
    }

    @PostMapping({"/page/reload", "page/reload"})
    public String reloadImportPageOnPost(Model model,
                                         @RequestAttribute("translationModel")TranslationModel translationModel,
                                         @RequestParam("selectedWordId") int wordId,
                                         @RequestParam("importId") int importId,
                                         @RequestParam("page") int page) {
        try {
            importPageService.loadImportWithTranslations(model, new LoadImportRequest(wordId, importId, page, translationModel));
            return "import-page/content";
        } catch (InvalidUrlException e) {
            return "error";
        }

    }

    @PutMapping({"/page/reload", "page/reload"})
    public String reloadImportPageOnPut(Model model,
                                        @RequestAttribute("translationModel")TranslationModel translationModel,
                                        @RequestParam("selectedWordId") int wordId,
                                        @RequestParam("importId") int importId,
                                        @RequestParam("page") int page) {
        try {
            importPageService.loadImportWithTranslations(model, new LoadImportRequest(wordId, importId, page, translationModel));
            return "import-page/content";
        } catch (InvalidUrlException e) {
            return "error";
        }
    }

    @GetMapping({"new", "/new"})
    public String addImportForm() {
        return "import-form/import-form";
    }

    @GetMapping({"{importId}", "/{importId}"})
    public String importPageContent(Model model,
                                    @PathVariable("importId") String importId,
                                    @RequestParam(name = "page", defaultValue = "1") int page) {
        try {
            importPageService.loadImportWithTranslations(model, new LoadImportRequest(0, Integer.parseInt(importId), page, null));
            return "import-page/import-page";
        } catch (InvalidUrlException e) {
            return "error";
        }
    }

    @PostMapping({"new", "/new"})
    public String addImport(Model model,
                            Authentication authentication,
                            @RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("url") String url) {
        try {
            UserProfile userProfile = userProfileService.findByUsername(authentication.getName());
            int importId = importService.addImport(model, AddImportRequest.builder()
                    .title(title)
                    .content(content)
                    .url(url)
                    .sourceLanguage(userProfile.sourceLanguage())
                    .targetLanguage(userProfile.targetLanguage())
                    .owner(authentication.getName())
                    .build());
            return "redirect:/imports/" + importId + "?page=1";

        } catch (UserNotFoundException e) {
            return "redirect:/auth/login";

        } catch (IOException e) {
            return "error";

        } catch (RetryViewException e) {
            return "import-form/import-form";
        }
    }

    @GetMapping({"by-url-form", "/by-url-form"})
    public String byUrlForm() {
        return "import-form/by-url-form";
    }

    @GetMapping({"insert-manually-form", "/insert-manually-form"})
    public String insertManuallyForm() {
        return "import-form/insert-manually-form";
    }
}
