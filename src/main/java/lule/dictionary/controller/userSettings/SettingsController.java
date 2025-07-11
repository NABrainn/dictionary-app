package lule.dictionary.controller.userSettings;

import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.interfaces.userProfile.CustomUserDetails;
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
    public String changeLanguage(@RequestParam("lang") String lang,
                                 Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userProfileService.updateTargetLanguage(userDetails.getUsername(), Language.valueOf(lang));
            return "/";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
