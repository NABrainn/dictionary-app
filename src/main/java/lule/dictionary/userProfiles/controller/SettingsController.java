package lule.dictionary.userProfiles.controller;

import lombok.RequiredArgsConstructor;
import lule.dictionary.userProfiles.service.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final UserProfileService userProfileService;

    @GetMapping({"/changeLanguage/target", "/changeLanguage/target/"})
    public String changeTargetLanguage(@RequestParam("lang") String language,
                                       Authentication authentication) {
        userProfileService.updateTargetLanguage(language, authentication);
        return "redirect:/";
    }

    @GetMapping({"/changeLanguage/source", "/changeLanguage/source/"})
    public String changeSourceLanguage(@RequestParam("lang") String language,
                                       Authentication authentication) {
        userProfileService.updateSourceLanguage(language, authentication);
        return "redirect:/";
    }

    @GetMapping({"/changeLanguage/ui", "/changeLanguage/ui/"})
    public String changeUILanguage(@RequestParam("lang") String language,
                                   Authentication authentication) {
        userProfileService.updateUILanguage(language, authentication);
        return "redirect:/";
    }

    @GetMapping({"/toggle", "/toggle/"})
    public String profilePanel(Model model,
                               Authentication authentication,
                               @RequestParam("item") String item) {
        boolean isNavbarOpen = userProfileService.toggleItem(item, authentication);
        model.addAttribute("isNavbarOpen", isNavbarOpen);
        return "navbar/profile-panel";
    }
}
