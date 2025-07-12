package lule.dictionary.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AuthController {
    String loginPage();
    String login(@RequestParam("login") @NonNull String login,
                 @RequestParam("password") @NonNull String password,
                 Model model,
                 RedirectAttributes redirectAttributes,
                 HttpServletResponse response);
    String signupPage();
    String signup(@RequestParam("login") @NonNull String login,
                  @RequestParam("email") @NonNull String email,
                  @RequestParam("password") @NonNull String password,
                  Model model);
    String logout(RedirectAttributes redirectAttributes,
                  HttpServletResponse response);
}
