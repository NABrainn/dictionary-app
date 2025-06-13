package lule.dictionary.controllerAdvice;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class JteControllerAdvice {

    @ModelAttribute
    public void csrf(Model model, CsrfToken csrf, Authentication authentication) {
        model.addAttribute("_csrf", csrf);
        if(authentication != null) model.addAttribute("authenticated", authentication.isAuthenticated());
        else model.addAttribute("authenticated", false);
    }
}
