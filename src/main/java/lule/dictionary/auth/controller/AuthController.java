package lule.dictionary.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.auth.data.request.LoginRequest;
import lule.dictionary.auth.service.AuthService;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.language.service.Language;
import lule.dictionary.errorLocalization.service.ErrorLocalization;
import lule.dictionary.session.service.SessionHelper;
import lule.dictionary.userProfiles.service.exception.UserExistsException;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import lule.dictionary.validation.service.ValidationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final ErrorLocalization errorLocalization;
    private final SessionHelper sessionHelper;

    @GetMapping({"/login", "/login/"})
    public String loginPage(Model model,
                            Authentication authentication,
                            HttpSession httpSession) {
        if(authentication != null) {
            return "redirect:/";
        }
        Language sourceLanguage = sessionHelper.getSystemLanguageInfo(httpSession);
        model.addAttribute("localization", authService.getLocalization(sourceLanguage));
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        Model model,
                        Authentication authentication,
                        HttpServletResponse response,
                        HttpSession httpSession) {
        if(authentication != null) {
            return "redirect:/";
        }
        try {
            authService.login(LoginRequest.of(login, password), response, httpSession);
            return "redirect:/";
        }
        catch (ValidationServiceException e) {
            Language sourceLanguage = sessionHelper.getSystemLanguageInfo(httpSession);
            var messages = errorLocalization.getMessageByViolation(e.getViolation(), sourceLanguage);
            log.warn("Login authentication failure, resending page: {}", e.getViolation());
            model.addAttribute("messages", errorLocalization.getMessageByViolation(e.getViolation(), sourceLanguage));
            model.addAttribute("localization", authService.getLocalization(sourceLanguage));
            return "auth/login";
        }
        catch (UserNotFoundException e) {
            log.info("UserNotFoundException exception: {}", e.getMessage());
            model.addAttribute("login", "User does not exist");
            return "auth/login";
        }
        catch (AuthenticationException e) {
            log.info("Authentication exception: {}", e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping({"/signup", "/signup/"})
    public String signupPage(Model model,
                             Authentication authentication,
                             HttpSession httpSession) {
        if(authentication != null) {
            return "redirect:/";
        }
        Language sourceLanguage = sessionHelper.getSystemLanguageInfo(httpSession);
        model.addAttribute("localization", authService.getLocalization(sourceLanguage));
        return "auth/signup";
    }

    @PostMapping({"/signup", "/signup/"})
    public String signup(@RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password,
                         Model model,
                         Authentication authentication,
                         HttpSession httpSession) {
        if(authentication != null) {
            return "redirect:/";
        }
        try {
            authService.signup(SignupRequest.of(login, email, password));
            return "redirect:/auth/login";
        }
        catch (ValidationServiceException e) {
            Language sourceLanguage = sessionHelper.getSystemLanguageInfo(httpSession);
            log.info(e.getMessage());
            model.addAttribute("messages", errorLocalization.getMessageByViolation(e.getViolation(), sourceLanguage));
            model.addAttribute("localization", authService.getLocalization(sourceLanguage));
            return "auth/signup";
        }

        catch (UserExistsException e) {
            log.info(e.getMessage());
            return "auth/signup";
        }
    }

    @PostMapping({"/logout", "/logout/"})
    public String logout(RedirectAttributes redirectAttributes,
                         HttpServletResponse response,
                         HttpSession httpSession) {
        authService.logout(response);
        Language sourceLanguage = sessionHelper.getSystemLanguageInfo(httpSession);
        redirectAttributes.addFlashAttribute("localization", authService.getLocalization(sourceLanguage));
        return "redirect:/auth/login";
    }
}
