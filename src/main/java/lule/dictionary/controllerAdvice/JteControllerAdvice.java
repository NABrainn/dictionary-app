package lule.dictionary.controllerAdvice;

import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.userProfile.AuthenticatedUserDataService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class JteControllerAdvice {

    private final AuthenticatedUserDataService dataService;

    @ModelAttribute
    public void csrf(Model model, CsrfToken csrf) {
        model.addAttribute("_csrf", csrf);
    }

    @ModelAttribute
    public void authenticated(Model model, Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()) model.addAttribute("authenticated", true);
        else model.addAttribute("authenticated", false);
    }
    @ModelAttribute
    public void username(Model model, Authentication authentication) {
        if(authentication == null) {
            model.addAttribute("username", "");
            return;
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("username", principal.getUsername());
    }

    @ModelAttribute
    public void sourceLanguage(Model model, Authentication authentication) {
        if(authentication == null) {
            model.addAttribute("sourceLanguage", Language.EN);
            return;
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("sourceLanguage", principal.sourceLanguage());
    }

    @ModelAttribute
    public void targetLanguage(Model model, Authentication authentication) {
        if(authentication == null) {
            model.addAttribute("targetLanguage", "English");
            return;
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute(
                "targetLanguage",
                dataService.getFullName(principal.targetLanguage())
        );
    }

    @ModelAttribute
    public void wordsLearnedCount(Model model, Authentication authentication) {
        if(authentication == null) {
            model.addAttribute("wordsLearned", 0);
            return;
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute(
                "wordsLearned",
                dataService.getWordsLearned(principal.getUsername())
        );
    }
}
