package lule.dictionary.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.osLanguage.SystemLanguageContext;
import lule.dictionary.service.auth.dto.request.imp.LoginRequest;
import lule.dictionary.service.auth.AuthService;
import lule.dictionary.service.auth.dto.request.imp.SignupRequest;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.localization.LocalizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImp implements AuthController {

    private final AuthService authService;
    private final LocalizationService localizationService;

    @GetMapping({"/login", "/login/"})
    public String loginPage(Model model) {
        model.addAttribute("navbarLocalization", localizationService.navbarLocalization(SystemLanguageContext.get()));
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        Model model,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {
        ServiceResult<?> result = authService.login(LoginRequest.of(login, password), response);
        if (result.hasError()) {
            log.warn("login authentication failure, resending page");
            model.addAttribute("result", result);
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
            return "auth/login";
        }
        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
        return "redirect:/";
    }

    @GetMapping({"/signup", "/signup/"})
    public String signupPage(Model model) {
        model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
        return "auth/signup";
    }

    @PostMapping({"/signup", "/signup/"})
    public String signup(@RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password,
                         Model model) {
        ServiceResult<?> result = authService.signup(SignupRequest.of(login, email, password));
        model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
        model.addAttribute("result", result);
        if(result.hasError()) {
            log.warn("signup authentication failure");
            return "auth/signup";
        }
        return "auth/login";
    }

    @PostMapping({"/logout", "/logout/"})
    public String logout(RedirectAttributes redirectAttributes,
                         HttpServletResponse response,
                         Model model) {
        ServiceResult<?> result = authService.logout(response);
        if(result.hasError()) {
            model.addAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
            log.warn("logout attempt failure");
            return "/error";
        }
        redirectAttributes.addFlashAttribute("navbarLocalization", localizationService.navbarLocalization(Language.EN));
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/auth/login";
    }
}
