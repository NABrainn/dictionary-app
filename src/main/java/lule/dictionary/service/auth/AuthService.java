package lule.dictionary.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import lule.dictionary.exception.RetryViewException;
import lule.dictionary.service.auth.dto.AuthRequest;
import lule.dictionary.service.auth.dto.LoginRequest;
import lule.dictionary.service.auth.dto.SignupRequest;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.cookie.CookieService;
import lule.dictionary.service.dto.ServiceResult;
import lule.dictionary.service.dto.ServiceResultFactory;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserProfileService userProfileService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final Validator validator;

    public void login(@NonNull Model clientModel,
                      @NonNull RedirectAttributes redirectAttributes,
                      @NonNull HttpServletResponse response,
                      @NonNull LoginRequest loginRequest) {
        try {
            var constraints = getConstraintViolations(loginRequest);
            if(!constraints.isEmpty()) {
                sendResultAndRetry(clientModel, ServiceResultFactory.createErrorResult(ErrorMapFactory.fromSet(constraints)));
            }
            UserProfile user = getUserProfile(loginRequest);
            Authentication authentication = getAuthentication(loginRequest, user);
            setAuthentication(authentication);
            setTimezoneOffset(user);
            sendJwtCookie(response, authentication);
            sendResult(redirectAttributes, ServiceResultFactory.createSuccessResult(Map.of()));

        } catch (AuthenticationException e) {
            sendResultAndRetry(clientModel, ServiceResultFactory.createErrorResult(Map.of("auth", "Authentication failed")));

        } catch (UserNotFoundException e) {
            sendResultAndRetry(clientModel, ServiceResultFactory.createErrorResult(Map.of("login", "User does not exist")));
        }
    }

    @Transactional
    public void signup(@NonNull Model model,
                       @NonNull String timeZone,
                       @NonNull SignupRequest signupRequest) {
        var constraints = getConstraintViolations(signupRequest);
        if(!constraints.isEmpty()) {
            sendResultAndRetry(model, ServiceResultFactory.createErrorResult(ErrorMapFactory.fromSet(constraints)));
        }
        Optional<UserProfile> optionalUserProfile = getUserProfile(signupRequest);
        if(optionalUserProfile.isPresent()) {
            sendResultAndRetry(model, ServiceResultFactory.createErrorResult(Map.of("login", "User with given username or email already exists.")));
        }

        String encodedPassword = getEncodedPassword(signupRequest);
        try {
            userProfileService.addUserProfile(signupRequest.login(), signupRequest.email(), encodedPassword, timeZone);
        } catch (UserExistsException e) {
            sendResultAndRetry(model, ServiceResultFactory.createErrorResult(Map.of()));
        }
        sendResult(model, ServiceResultFactory.createSuccessResult(Map.of()));
    }

    public void logout(@NonNull RedirectAttributes redirectAttributes,
                       @NonNull HttpServletResponse httpServletResponse) {
        clearAuthentication();
        deleteJwtCookie(httpServletResponse);
        sendResult(redirectAttributes, ServiceResultFactory.createSuccessResult(Map.of()));
    }

    private void clearAuthentication() {
        setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    private String getEncodedPassword(SignupRequest signupRequest) {
        String encodedPassword = bCryptPasswordEncoder.encode(signupRequest.password());
        return encodedPassword;
    }

    private Optional<UserProfile> getUserProfile(SignupRequest signupRequest) {
        Optional<UserProfile> optionalUserProfile = userProfileService.findByUsernameOrEmail(signupRequest.login(), signupRequest.email());
        return optionalUserProfile;
    }

    private void sendResultAndRetry(Model model, ServiceResult result) throws RetryViewException {
        sendResult(model, result);
        String exceptionMessage = readMessageFromResult(result);
        throw new RetryViewException(exceptionMessage);
    }

    private void sendResult(Model clientModel, ServiceResult serviceResult) {
        clientModel.addAttribute("result", serviceResult);
    }

    private String readMessageFromResult(ServiceResult result) throws RuntimeException {
        return result.errors().values().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ServiceResult is lacking message values"));
    }

    private void deleteJwtCookie(HttpServletResponse response) {
        Cookie cookie = cookieService.deleteJwtCookie("jwt");
        response.addCookie(cookie);
    }

    private Set<ConstraintViolation<@NonNull AuthRequest>> getConstraintViolations(AuthRequest authRequest) {
        return validator.validate(authRequest);
    }


    private void sendJwtCookie(HttpServletResponse response, Authentication authentication) {
        Cookie jwtCookie = createJwtCookie(authentication);
        response.addCookie(jwtCookie);
    }

    private Cookie createJwtCookie(Authentication authentication) {
        Cookie jwtCookie = cookieService.createJwtCookie("jwt", jwtService.generateTokenPair(authentication));
        return jwtCookie;
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setTimezoneOffset(UserProfile user) {
        TimeZoneOffsetContext.set(user.offset());
    }

    private Authentication getAuthentication(LoginRequest loginRequest, UserProfile user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.username(),
                        loginRequest.password()
                )
        );
        return authentication;
    }

    private UserProfile getUserProfile(LoginRequest loginRequest) {
        UserProfile user = userProfileService.findByUsername(loginRequest.login());
        return user;
    }
}
