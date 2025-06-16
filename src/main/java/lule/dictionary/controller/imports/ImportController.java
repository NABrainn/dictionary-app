package lule.dictionary.controller.imports;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.imports.importService.dto.AddImportRequest;
import lule.dictionary.service.imports.importPageService.dto.SaveTranslationRequest;
import lule.dictionary.dto.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.imports.importPageService.ImportPageService;
import lule.dictionary.service.imports.importService.ImportService;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({"/imports", "/imports/"})
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;
    private final UserProfileService userProfileService;
    private final ImportPageService importPageService;

    @GetMapping({"new", "/new"})
    public String addImportForm() {
        return "import-form/import-form";
    }

    @GetMapping({"{importId}", "/{importId}"})
    public String importPageContent(Model model,
                                    @PathVariable("importId") String importId) {
        importPageService.loadImportWithTranslations(model, new SaveTranslationRequest(0, Integer.parseInt(importId)));
        return "import-page/import-page";
    }

    @PostMapping({"new", "/new"})
    public String addImport(
                            Authentication authentication,
                            @RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("url") String url) {
        UserProfile userProfile = userProfileService.findByUsername(authentication.getName());
        int importId = importService.addImport(AddImportRequest.builder()
                        .title(title)
                        .content(content)
                        .url(url)
                        .sourceLanguage(userProfile.sourceLanguage())
                        .targetLanguage(userProfile.targetLanguage())
                        .owner(authentication.getName())
                .build());
        return "redirect:/imports/" + importId;
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
