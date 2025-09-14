package lule.dictionary.userProfiles.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.session.service.SessionHelper;
import lule.dictionary.userProfiles.data.UserProfile;
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
    private final SessionHelper sessionHelper;

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

    @GetMapping({"/profilePanel", "/profilePanel/"})
    public String profilePanel(HttpSession session,
                               Model model) {
        boolean isProfileOpen = session.getAttribute("isProfileOpen") != null && (boolean) session.getAttribute("isProfileOpen");
        session.setAttribute("isProfileOpen", !isProfileOpen);
        model.addAttribute("isProfileOpen", !isProfileOpen);
        return "navbar/profile-panel";
    }
}
