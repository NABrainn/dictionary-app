package lule.dictionary.controllerAdvice;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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
}
