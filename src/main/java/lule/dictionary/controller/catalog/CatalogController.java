package lule.dictionary.controller.catalog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.controller.catalog.dto.AddImportRequest;
import lule.dictionary.controller.catalog.dto.SaveTranslationRequest;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.service.imports.ImportService;
import lule.dictionary.service.userProfile.UserProfileService;
import lule.dictionary.service.console.DictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping({"/catalog", "/catalog/"})
@RequiredArgsConstructor
public class CatalogController {

    private final ImportService importService;
    private final UserProfileService userProfileService;
    private final DictionaryService dictionaryService;

    @GetMapping({"", "/"})
    public String index() {
        return "catalog";
    }

    @GetMapping({"/page", "page"})
    public String findAll(Model model) throws IOException {
//        userProfileService.addUserProfile(UserProfileFactory.createUserProfile(
//                UserProfileFactory.createCredentials(
//                        "username",
//                        "user@name.com",
//                        "password"
//                ),
//                UserProfileFactory.createSettings(
//                        Language.EN,
//                        Language.NO
//                )
//        ));
        UserProfile user = userProfileService.findByUsername("username");
        int importId = importService.addImport(model, new AddImportRequest("kull title", "yr.no", user.userProfileSettings(), user.userProfileCredentials().username()));
        dictionaryService.loadImportWithTranslations(model, new SaveTranslationRequest(0, importId));
        return "importPage";
    }
    @PostMapping({"/page/reload", "page/reload"})
    public String reloadOnPost(Model model,
                               @RequestParam("selectedWordId") int wordId,
                               @RequestParam("importId") int importId) {
        dictionaryService.loadImportWithTranslations(model, new SaveTranslationRequest(wordId, importId));
        return "importPageContent";
    }
    @PutMapping({"/page/reload", "page/reload"})
    public String reloadOnPut(Model model,
                              @RequestParam("selectedWordId") int wordId,
                              @RequestParam("importId") int importId) {
        dictionaryService.loadImportWithTranslations(model, new SaveTranslationRequest(wordId, importId));
        return "importPageContent";
    }
}
