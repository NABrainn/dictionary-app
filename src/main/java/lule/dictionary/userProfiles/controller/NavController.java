package lule.dictionary.userProfiles.controller;

import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.userProfiles.data.ChangeSourceLanguageRequest;
import lule.dictionary.userProfiles.data.ChangeTargetLanguageRequest;
import lule.dictionary.userProfiles.data.ChangeUiLanguageRequest;
import lule.dictionary.userProfiles.service.NavService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/nav")
public class NavController {

    private final NavService navService;

    @GetMapping({"/changeLanguage", "/changeLanguage/"})
    public String changeLanguage(@RequestParam("type") String type,
                                 @RequestParam("lang") String languageString,
                                 Authentication authentication) {
        var language = Language.valueOf(languageString);
        var request = switch (type) {
            case "ui" -> ChangeUiLanguageRequest.of(language);
            case "source" -> ChangeSourceLanguageRequest.of(language);
            case "target" -> ChangeTargetLanguageRequest.of(language);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported type provided: " + type);
        };
        navService.updateLanguage(request, authentication);
        return "redirect:/";
    }

    @GetMapping({"/toggle", "/toggle/"})
    public String toggle(@RequestParam("item") String item,
                         Authentication authentication) {
        boolean open = navService.toggle(item, authentication);
        return open ?
                "navbar/navbar-with-open-panel" :
                "navbar/navbar-with-closed-panel";
    }
}
