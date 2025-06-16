package lule.dictionary.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.service.auth.dto.LoginRequest;
import lule.dictionary.service.auth.dto.SignupRequest;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.exception.ResourceNotFoundException;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.service.auth.validator.AuthValidator;
import lule.dictionary.service.auth.validator.exception.ValidationException;
import lule.dictionary.service.cookie.CookieService;
import lule.dictionary.service.dto.ServiceResult;
import lule.dictionary.service.jwt.JwtService;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserProfileService userProfileService;
    private final AuthValidator authValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public void login(@NonNull Model model, @NonNull RedirectAttributes redirectAttributes, @NonNull HttpServletResponse response, @NonNull LoginRequest loginRequest) {
        try {
            String login = authValidator.validateUsername(loginRequest.login());
            String password = authValidator.validatePassword(loginRequest.password());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login,
                            password
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.addCookie(cookieService.createJwtCookie("jwt", jwtService.generateTokenPair(authentication)));
            redirectAttributes.addFlashAttribute("result", new ServiceResult(false, ""));
        } catch (AuthenticationException | ResourceNotFoundException | ValidationException e) {
            model.addAttribute("result", new ServiceResult(true, e.getMessage()));
            model.addAttribute("authentication", null);
            throw new ServiceException(e.getMessage());
        }
    }

    public void signup(@NonNull Model model, @NonNull SignupRequest signupRequest) {
        try {
            String login = authValidator.validateUsername(signupRequest.username());
            String email = authValidator.validateEmail(signupRequest.email());
            String password = authValidator.validatePassword(signupRequest.password());

            Optional<UserProfile> optionalUserProfile = userProfileService.findByUsernameOrEmail(login, email);
            if(optionalUserProfile.isPresent()) {
                String error = "User with given username or email already exists.";
                model.addAttribute("result", new ServiceResult(true, error));
                throw new ServiceException(error);
            }
            String encodedPassword = bCryptPasswordEncoder.encode(password);
            userProfileService.addUserProfile(login, email, encodedPassword);
            model.addAttribute("result", new ServiceResult(false, ""));
        } catch (ValidationException e) {
            model.addAttribute("result", new ServiceResult(true, e.getMessage()));
            throw new ServiceException(e.getMessage());
        }
    }

    public void logout(
            @NonNull RedirectAttributes redirectAttributes,
            @NonNull HttpServletResponse httpServletResponse) {
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        Cookie cookie = cookieService.deleteJwtCookie("jwt");
        httpServletResponse.addCookie(cookie);
        redirectAttributes.addFlashAttribute("result", new ServiceResult(false, ""));
    }
}
