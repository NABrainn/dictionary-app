package lule.dictionary.userProfiles.controller;

import lombok.RequiredArgsConstructor;
import lule.dictionary.userProfiles.service.NavService;
import lule.dictionary.userProfiles.service.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/nav")
public class NavController {

    private final UserProfileService userProfileService;
    private final NavService navService;

    @GetMapping({"/changeLanguage/target", "/changeLanguage/target/"})
    public String changeTargetLanguage(@RequestParam("lang") String language,
                                       Authentication authentication) {
        navService.updateLanguage(language, authentication);
        return "redirect:/";
    }

    @GetMapping({"/changeLanguage/source", "/changeLanguage/source/"})
    public String changeSourceLanguage(@RequestParam("lang") String language,
                                       Authentication authentication) {
        navService.updateLanguage(language, authentication);
        return "redirect:/";
    }

    @GetMapping({"/changeLanguage/ui", "/changeLanguage/ui/"})
    public String changeUILanguage(@RequestParam("lang") String language,
                                   Authentication authentication) {
        navService.updateLanguage(language, authentication);
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
