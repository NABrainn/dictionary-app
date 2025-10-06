package lule.dictionary.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.core.Authentication;
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
                            Authentication authentication) {
        if(authentication != null) {
            return "redirect:/";
        }
        model.addAttribute("error", Map.of());
        model.addAttribute("localization", authService.getTextLocalization());
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        Model model,
                        Authentication authentication,
                        HttpServletResponse response) {
        if(authentication != null) {
            return "redirect:/";
        }
        Result<?> result = authService.login(LoginRequest.of(login, password), response);
        return switch (result) {
            case Ok<?> _ -> "redirect:/";
            case Err<?> v -> {
                if(v.throwable() instanceof AuthServiceException authServiceException) {
                    model.addAttribute("error", authServiceException.getViolation());
                    model.addAttribute("localization", authService.getTextLocalization());
                    yield  "auth/login";
                }
                yield  "error";
            }
        };
    }

    @GetMapping({"/signup", "/signup/"})
    public String signupPage(Model model,
                             Authentication authentication) {
        if(authentication != null) {
            return "redirect:/";
        }
        model.addAttribute("error", Map.of());
        model.addAttribute("localization", authService.getTextLocalization());
        return "auth/signup";
    }

    @PostMapping({"/signup", "/signup/"})
    public String signup(@RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password,
                         Model model,
                         Authentication authentication) {
        if(authentication != null) {
            return "redirect:/";
        }
        Result<?> result = authService.signup(SignupRequest.of(login, email, password));
        return switch (result) {
            case Ok<?> v -> {
                model.addAttribute("error", Map.of());
                model.addAttribute("localization", authService.getTextLocalization());
                yield  "redirect:/auth/login";
            }
            case Err<?> v -> {
                if(v.throwable() instanceof AuthServiceException authServiceException) {
                    model.addAttribute("error", authServiceException.getViolation());
                    model.addAttribute("localization", authService.getTextLocalization());
                    yield  "auth/signup";
                }
                yield "error";
            }
        };
    }
}
