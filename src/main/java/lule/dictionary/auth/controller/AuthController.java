package lule.dictionary.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AuthController {
    String loginPage(Model model,
                     RedirectAttributes redirectAttributes,
                     Authentication authentication,
                     HttpSession httpSession);
    String login(@RequestParam("login") @NonNull String login,
                 @RequestParam("password") @NonNull String password,
                 Model model,
                 RedirectAttributes redirectAttributes,
                 Authentication authentication,
                 HttpServletResponse response,
                 HttpSession httpSession);
    String signupPage(Model model,
                      RedirectAttributes redirectAttributes,
                      Authentication authentication,
                      HttpSession httpSession);
    String signup(@RequestParam("login") @NonNull String login,
                  @RequestParam("email") @NonNull String email,
                  @RequestParam("password") @NonNull String password,
                  Model model,
                  RedirectAttributes redirectAttributes,
                  Authentication authentication,
                  HttpSession httpSession);
    String logout(RedirectAttributes redirectAttributes,
                  HttpServletResponse response,
                  Model model,
                  Authentication authentication,
                  HttpSession httpSession);
}
