package lule.dictionary.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.auth.data.exception.AuthServiceException;
import lule.dictionary.auth.data.request.LoginRequest;
import lule.dictionary.auth.service.AuthService;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.userProfiles.service.exception.UserExistsException;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import lule.dictionary.validation.data.ValidationException;
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

    @GetMapping({"/login", "/login/"})
    public String loginPage(Model model,
                            Authentication authentication,
                            HttpSession httpSession) {
        if(authentication != null) {
            return "redirect:/";
        }
        model.addAttribute("error", Map.of());
        model.addAttribute("localization", authService.getTextLocalization(httpSession));
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        Model model,
                        Authentication authentication,
                        HttpServletResponse response,
                        HttpSession session) {
        if(authentication != null) {
            return "redirect:/";
        }
        Result<?> result = authService.login(LoginRequest.of(login, password), response, session);
        return switch (result) {
            case Ok<?> v -> "redirect:/";
            case Err<?> v -> {
                if(v.throwable() instanceof AuthServiceException authServiceException) {
                    model.addAttribute("error", authServiceException.getViolation());
                    model.addAttribute("localization", authService.getTextLocalization(session));
                    yield  "auth/login";
                }
                yield  "error";
            }
        };
    }

    @GetMapping({"/signup", "/signup/"})
    public String signupPage(Model model,
                             Authentication authentication,
                             HttpSession httpSession) {
        if(authentication != null) {
            return "redirect:/";
        }
        model.addAttribute("error", Map.of());
        model.addAttribute("localization", authService.getTextLocalization(httpSession));
        return "auth/signup";
    }

    @PostMapping({"/signup", "/signup/"})
    public String signup(@RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password,
                         Model model,
                         Authentication authentication,
                         HttpSession session) {
        if(authentication != null) {
            return "redirect:/";
        }
        Result<?> result = authService.signup(SignupRequest.of(login, email, password), session);
        return switch (result) {
            case Ok<?> v -> {
                model.addAttribute("error", Map.of());
                model.addAttribute("localization", authService.getTextLocalization(session));
                yield  "redirect:/auth/login";
            }
            case Err<?> v -> {
                if(v.throwable() instanceof AuthServiceException authServiceException) {
                    model.addAttribute("error", authServiceException.getViolation());
                    model.addAttribute("localization", authService.getTextLocalization(session));
                    yield  "auth/signup";
                }
                yield "error";
            }
        };
    }

    @PostMapping({"/logout", "/logout/"})
    public String logout(RedirectAttributes redirectAttributes,
                         Model model,
                         HttpServletResponse response,
                         HttpSession httpSession) {
        authService.logout(response);
        model.addAttribute("error", Map.of());
        redirectAttributes.addFlashAttribute("localization", authService.getTextLocalization(httpSession));
        return "redirect:/auth/login";
    }
}
