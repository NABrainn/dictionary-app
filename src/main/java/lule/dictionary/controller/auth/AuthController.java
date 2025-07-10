package lule.dictionary.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.service.auth.dto.request.AuthRequestFactory;
import lule.dictionary.service.auth.dto.request.imp.LoginRequest;
import lule.dictionary.service.auth.AuthService;
import lule.dictionary.service.dto.result.ServiceResult;
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
public class AuthController {

    private final AuthService authService;
    private final AuthRequestFactory authRequestFactory;

    @GetMapping({"/login", "/login/"})
    public String loginView() {
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        Model model,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {
        ServiceResult<?> result = requestLogin(authRequestFactory.ofLoginRequest(login, password), response);
        if (result.hasError()) {
            model.addAttribute("result", result);
            return "auth/login";
        }
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/";
    }

    @GetMapping({"/signup", "/signup/"})
    public String signupView() {
        return "auth/signup";
    }

    @PostMapping({"/signup", "/signup/"})
    public String signup(@RequestParam("login") String login,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password,
                         Model model) {
        ServiceResult<?> result = requestSignup(login, email, password);
        model.addAttribute("result", result);
        if(result.hasError()) {
            return "auth/signup";
        }
        return "auth/login";
    }

    @PostMapping({"/logout", "/logout/"})
    public String logout(RedirectAttributes redirectAttributes,
                         HttpServletResponse response) {
        ServiceResult<?> result = requestLogout(response);
        if(result.hasError()) {
            return "/error";
        }
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/auth/login";
    }

    private ServiceResult<?> requestLogin(LoginRequest loginRequest, HttpServletResponse response) {
        return authService.login(authRequestFactory.ofLoginRequest(loginRequest.login(), loginRequest.password()), response);
    }

    private ServiceResult<?> requestSignup(String login, String email, String password) {
        return authService.signup(authRequestFactory.ofSignupRequest(login, email, password));
    }

    private ServiceResult<?> requestLogout(HttpServletResponse response) {
        return authService.logout(response);
    }
}
