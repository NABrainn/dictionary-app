package lule.dictionary.controllerAdvice;

import lule.dictionary.entity.application.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.enumeration.Language;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class JteControllerAdvice {

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
            model.addAttribute("targetLanguage", Language.NO);
            return;
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("targetLanguage", principal.targetLanguage());
    }
}
