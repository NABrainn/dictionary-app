package lule.dictionary.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.exception.RetryViewException;
import lule.dictionary.service.auth.dto.LoginRequest;
import lule.dictionary.service.auth.dto.SignupRequest;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.cookie.CookieService;
import lule.dictionary.service.dto.ServiceResult;
import lule.dictionary.service.userProfile.exception.UserExistsException;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import lule.dictionary.util.errors.ErrorMapFactory;
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

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserProfileService userProfileService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final Validator validator;

    public void login(@NonNull Model model,
                      @NonNull RedirectAttributes redirectAttributes,
                      @NonNull HttpServletResponse response,
                      @NonNull LoginRequest loginRequest) {
        try {
            var constraints = validator.validate(loginRequest);
            if(!constraints.isEmpty()) {
                var result = new ServiceResult(true, ErrorMapFactory.fromSet(constraints));
                model.addAttribute("result", result);
                throw new RetryViewException("validation failure");
            }
            UserProfile user = userProfileService.findByUsername(loginRequest.login());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.username(),
                            loginRequest.password()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.addCookie(cookieService.createJwtCookie("jwt", jwtService.generateTokenPair(authentication)));
            var result = new ServiceResult(false, Map.of());
            redirectAttributes.addFlashAttribute("result", result);

        } catch (AuthenticationException e) {
            var result = new ServiceResult(false, Map.of());
            model.addAttribute("result", result);
            throw new RetryViewException("Authentication exception");

        } catch (UserNotFoundException e) {
            var result = new ServiceResult(true, Map.of("login", "User does not exist"));
            model.addAttribute("result", result);
            model.addAttribute("authentication", null);
            throw new RetryViewException("User does not exist");
        }
    }

    public void signup(@NonNull Model model,
                       @NonNull SignupRequest signupRequest) {
        var constraints = validator.validate(signupRequest);

        if(!constraints.isEmpty()) {
            var result = new ServiceResult(true, ErrorMapFactory.fromSet(constraints));
            model.addAttribute("result", result);
            throw new RetryViewException("validation failure");
        }

        Optional<UserProfile> optionalUserProfile = userProfileService.findByUsernameOrEmail(signupRequest.login(), signupRequest.email());
        if(optionalUserProfile.isPresent()) {
            var result = new ServiceResult(true, Map.of("login", "User with given username or email already exists."));
            model.addAttribute("result", result);
            throw new RetryViewException("User with given username already exists.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(signupRequest.password());

        try {
            userProfileService.addUserProfile(signupRequest.login(), signupRequest.email(), encodedPassword);
        } catch (UserExistsException e) {
            var result = new ServiceResult(true, Map.of());
            model.addAttribute("result", result);
            throw new RetryViewException(e.getMessage());
        }

        var result = new ServiceResult(false, Map.of());
        model.addAttribute("result", result);

    }

    public void logout(@NonNull RedirectAttributes redirectAttributes,
                       @NonNull HttpServletResponse httpServletResponse) {
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        Cookie cookie = cookieService.deleteJwtCookie("jwt");
        httpServletResponse.addCookie(cookie);
        redirectAttributes.addFlashAttribute("result", new ServiceResult(false, Map.of()));
    }
}
