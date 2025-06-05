package lule.dictionary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.dto.web.ImportForm;
import lule.dictionary.enumeration.Language;
import lule.dictionary.factory.dto.UserProfileFactory;
import lule.dictionary.service.application.ImportData;
import lule.dictionary.service.application.entity.ImportService;
import lule.dictionary.service.application.entity.UserProfileService;
import lule.dictionary.service.application.integration.DictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping({"/imports", "/imports/"})
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;
    private final UserProfileService userProfileService;
    private final DictionaryService dictionaryService;

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
        int importId = dictionaryService.addImport("kull title", "yr.no", user.userProfileSettings(), user.userProfileCredentials().username());
        ImportData importData = dictionaryService.loadImport(importId);
        int wordId = 0;
        model.addAttribute("data", importData);
        model.addAttribute("selectedWordId", wordId);
        return "importPage";
    }
    @PostMapping({"/page/reload", "page/reload"})
    public String reloadOnPost(Model model,
                               @RequestParam("selectedWordId") int wordId) throws IOException {
        UserProfile user = userProfileService.findByUsername("username");
        int importId = dictionaryService.addImport("kull title", "yr.no", user.userProfileSettings(), user.userProfileCredentials().username());
        ImportData importData = dictionaryService.loadImport(importId);
        model.addAttribute("data", importData);
        model.addAttribute("selectedWordId", wordId);
        return "importPageContent";
    }
    @PutMapping({"/page/reload", "page/reload"})
    public String reloadOnPut(Model model,
                              @RequestParam("selectedWordId") int wordId) throws IOException {
        UserProfile user = userProfileService.findByUsername("username");
        int importId = dictionaryService.addImport("kull title", "yr.no", user.userProfileSettings(), user.userProfileCredentials().username());
        ImportData importData = dictionaryService.loadImport(importId);
        model.addAttribute("data", importData);
        model.addAttribute("selectedWordId", wordId);
        return "importPageContent";
    }
}
