package lule.dictionary.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.exception.RetryViewException;
import lule.dictionary.service.auth.dto.LoginRequest;
import lule.dictionary.service.auth.dto.SignupRequest;
import lule.dictionary.service.auth.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @GetMapping("login")
    public String loginView() {
        return "auth/login.jte";
    }

    @PostMapping("login")
    public String login(Model model,
                        RedirectAttributes redirectAttributes,
                        @RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        HttpServletResponse response) {
        try {
            authService.login(model, redirectAttributes, response, new LoginRequest(login, password));
            return "redirect:/";
        } catch (RetryViewException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            return "auth/login.jte";
        }
    }

    @GetMapping("signup")
    public String signupView() {
        return "auth/signup.jte";
    }

    @PostMapping("signup")
    public String signup(@NonNull Model model,
                         @RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password) {
        try {
            authService.signup(model, new SignupRequest(login, email, password));
            return "auth/login.jte";
        } catch (RetryViewException e) {
            log.warn("Retrying view due to input issue: {}", e.getMessage());
            return "auth/signup.jte";
        }
    }

    @PostMapping("logout")
    public String logout(@NonNull RedirectAttributes redirectAttributes, HttpServletResponse response) {
        authService.logout(
                redirectAttributes,
                response
        );
        return "redirect:/auth/login.jte";
    }
}
