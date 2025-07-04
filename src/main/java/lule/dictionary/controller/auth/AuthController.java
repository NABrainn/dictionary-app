package lule.dictionary.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import lule.dictionary.service.auth.dto.LoginRequest;
import lule.dictionary.service.auth.dto.SignupRequest;
import lule.dictionary.service.auth.AuthService;
import lule.dictionary.service.dto.ServiceResult;
import lule.dictionary.util.DateUtil;
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

    @GetMapping({"/login", "/login/"})
    public String loginView() {
        return "auth/login";
    }

    @PostMapping({"/login", "/login/"})
    public String login(Model model,
                        RedirectAttributes redirectAttributes,
                        @RequestParam("login") @NonNull String login,
                        @RequestParam("password") @NonNull String password,
                        HttpServletResponse response) {
            ServiceResult result = authService.login(response, new LoginRequest(login, password));
            if (result.error()) {
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
    public String signup(Model model,
                         @RequestParam("login") @NonNull String login,
                         @RequestParam("email") @NonNull String email,
                         @RequestParam("password") @NonNull String password) {
            String timeZoneId = DateUtil.stringToZoneOffset(TimeZoneOffsetContext.get()).getId();
            ServiceResult result = authService.signup(timeZoneId, new SignupRequest(login, email, password));
            model.addAttribute("result", result);
            if(result.error()) {
                return "auth/signup";
            }
            return "auth/login";
    }

    @PostMapping({"/logout", "/logout/"})
    public String logout(@NonNull RedirectAttributes redirectAttributes, HttpServletResponse response) {
        ServiceResult result = authService.logout(response);
        if(result.error()) {
            return "/error";
        }
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/auth/login";
    }
}
