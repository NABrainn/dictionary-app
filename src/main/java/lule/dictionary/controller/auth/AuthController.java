package lule.dictionary.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.controller.auth.dto.LoginRequest;
import lule.dictionary.controller.auth.dto.SignupRequest;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.service.auth.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/auth", "/auth/"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @GetMapping({"/login", "/login/"})
    public String loginView() {
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@NonNull Model model,
                        @RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        try {
            authService.login(model, response, new LoginRequest(login, password));
            return "catalog";
        } catch (ServiceException e) {
            return "auth/login";
        }
    }

    @GetMapping({"/signup", "/signup/"})
    public String signupView() {
        return "auth/signup";
    }

    @PostMapping({"/signup", "/signup/"})
    public String signup(@NonNull Model model,
                         @RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password) {
        try {
            authService.signup(model, new SignupRequest(login, email, password));
            return "catalog";
        } catch (ServiceException e) {
            return "auth/signup";
        }
    }
}
