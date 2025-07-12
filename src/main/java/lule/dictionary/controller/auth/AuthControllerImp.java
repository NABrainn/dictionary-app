package lule.dictionary.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.service.auth.dto.request.imp.LoginRequest;
import lule.dictionary.service.auth.AuthService;
import lule.dictionary.service.auth.dto.request.imp.SignupRequest;
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
@Slf4j
public class AuthControllerImp implements AuthController {

    private final AuthService authService;

    @GetMapping({"/login", "/login/"})
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        Model model,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {
        ServiceResult<?> result = requestLogin(LoginRequest.of(login, password), response);
        if (result.hasError()) {
            log.warn("login authentication failure, resending page");
            model.addAttribute("result", result);
            return "auth/login";
        }
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/";
    }

    @GetMapping({"/signup", "/signup/"})
    public String signupPage() {
        return "auth/signup";
    }

    @PostMapping({"/signup", "/signup/"})
    public String signup(@RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password,
                         Model model) {
        ServiceResult<?> result = requestSignup(SignupRequest.of(login, email, password));
        model.addAttribute("result", result);
        if(result.hasError()) {
            log.warn("signup authentication failure");
            return "auth/signup";
        }
        return "auth/login";
    }

    @PostMapping({"/logout", "/logout/"})
    public String logout(RedirectAttributes redirectAttributes,
                         HttpServletResponse response) {
        ServiceResult<?> result = requestLogout(response);
        if(result.hasError()) {
            log.warn("logout attempt failure");
            return "/error";
        }
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/auth/login";
    }

    private ServiceResult<?> requestLogin(LoginRequest loginRequest, HttpServletResponse response) {
        return authService.login(loginRequest, response);
    }

    private ServiceResult<?> requestSignup(SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    private ServiceResult<?> requestLogout(HttpServletResponse response) {
        return authService.logout(response);
    }
}
