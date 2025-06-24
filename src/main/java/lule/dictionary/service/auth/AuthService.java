package lule.dictionary.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.service.auth.dto.LoginRequest;
import lule.dictionary.service.auth.dto.SignupRequest;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.exception.ResourceNotFoundException;
import lule.dictionary.service.cookie.CookieService;
import lule.dictionary.service.dto.ServiceResult;
import lule.dictionary.util.errors.ServiceResultFactory;
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
            var result = validator.validate(loginRequest);
            if(!result.isEmpty()) {
                model.addAttribute("result", new ServiceResult(true, ServiceResultFactory.fromSet(result)));
                return;
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.login(),
                            loginRequest.password()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.addCookie(cookieService.createJwtCookie("jwt", jwtService.generateTokenPair(authentication)));
            redirectAttributes.addFlashAttribute("result", new ServiceResult(false, Map.of()));
        } catch (AuthenticationException | ResourceNotFoundException e) {
            model.addAttribute("result", new ServiceResult(true, Map.of()));
            model.addAttribute("authentication", null);
        }
    }

    public void signup(@NonNull Model model,
                       @NonNull SignupRequest signupRequest) {
        var result = validator.validate(signupRequest);
        if(!result.isEmpty()) {
            model.addAttribute("result", new ServiceResult(true, ServiceResultFactory.fromSet(result)));
            return;
        }
        Optional<UserProfile> optionalUserProfile = userProfileService.findByUsernameOrEmail(signupRequest.login(), signupRequest.email());
        if(optionalUserProfile.isPresent()) {
            model.addAttribute("result", new ServiceResult(true, Map.of("found", "User with given username or email already exists.")));
        }
        String encodedPassword = bCryptPasswordEncoder.encode(signupRequest.password());
        userProfileService.addUserProfile(signupRequest.login(), signupRequest.email(), encodedPassword);
        model.addAttribute("result", new ServiceResult(false, Map.of()));
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
