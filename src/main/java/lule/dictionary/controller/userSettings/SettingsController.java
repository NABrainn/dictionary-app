package lule.dictionary.controller.userSettings;

import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final UserProfileService userProfileService;

    @GetMapping({"/changeLanguage", "/changeLanguage/"})
    public String changeLanguage(@RequestParam("lang") String language,
                                 Authentication authentication) {
        String username = extractUsername(authentication);
        updateTargetLanguage(username, language);
        return "redirect:/";
    }

    private void updateTargetLanguage(String username, String language) {
        userProfileService.updateTargetLanguage(username, Language.valueOf(language));
    }

    private String extractUsername(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
