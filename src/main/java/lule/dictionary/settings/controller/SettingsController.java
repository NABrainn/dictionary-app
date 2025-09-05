package lule.dictionary.settings.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.language.service.Language;
import lule.dictionary.session.service.SessionHelper;
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
        String username = extractUsername(authentication);
        updateTargetLanguage(username, language);
        return "redirect:/";
    }

    @GetMapping({"/changeLanguage/source", "/changeLanguage/source/"})
    public String changeSourceLanguage(@RequestParam("lang") String language,
                                       Authentication authentication) {
        String username = extractUsername(authentication);
        updateSourceLanguage(username, language);
        return "redirect:/";
    }

    @GetMapping({"/changeLanguage/ui", "/changeLanguage/ui/"})
    public String changeUILanguage(@RequestParam("lang") String language,
                                   Authentication authentication) {
        String username = extractUsername(authentication);
        updateUILanguage(username, language);
        return "redirect:/";
    }

    @GetMapping({"/profilePanel", "/profilePanel/"})
    public String profilePanel(HttpSession httpSession,
                               Model model) {
        boolean isProfileOpen = sessionHelper.getOrFalse(httpSession, "isProfileOpen");
        httpSession.setAttribute("isProfileOpen", !isProfileOpen);
        model.addAttribute("isProfileOpen", !isProfileOpen);
        return "navbar/profile-panel";
    }

    private void updateTargetLanguage(String username, String language) {
        userProfileService.updateTargetLanguage(username, Language.valueOf(language));
    }

    private void updateSourceLanguage(String username, String language) {
        userProfileService.updateSourceLanguage(username, Language.valueOf(language));
    }

    private void updateUILanguage(String username, String language) {
        userProfileService.updateUILanguage(username, Language.valueOf(language));
    }

    private String extractUsername(Authentication authentication) {
        UserProfile userDetails = (UserProfile) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
