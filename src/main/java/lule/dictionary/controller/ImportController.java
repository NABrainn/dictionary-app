package lule.dictionary.controller;

import lombok.RequiredArgsConstructor;
import lule.dictionary.controller.catalog.dto.AddImportRequest;
import lule.dictionary.controller.catalog.dto.SaveTranslationRequest;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.factory.dto.UserProfileFactory;
import lule.dictionary.service.console.ImportPageService;
import lule.dictionary.service.imports.ImportService;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/imports", "/imports/"})
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;
    private final UserProfileService userProfileService;
    private final ImportPageService importPageService;

    @GetMapping({"new", "/new"})
    public String addImportForm(Authentication authentication, Model model) {
        model.addAttribute("authentication", authentication);
        return "import-form/import-form";
    }

    @PostMapping({"new", "/new"})
    public String addImport(Model model,
                            Authentication authentication,
                            @RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("url") String url) {
        UserProfile userProfile = userProfileService.findByUsername(authentication.getName());
        int importId = importService.addImport(authentication, model, new AddImportRequest(
                title,
                content,
                url,
                UserProfileFactory.createSettings(userProfile.userProfileSettings().sourceLanguage(), userProfile.userProfileSettings().targetLanguage()),
                authentication.getName()));
        importPageService.loadImportWithTranslations(model, new SaveTranslationRequest(0, importId));
        return "import-page/import-page-content";
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
