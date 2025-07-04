package lule.dictionary.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserProfileService userProfileService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final Validator validator;

    public ServiceResult login(@NonNull HttpServletResponse response,
                               @NonNull LoginRequest loginRequest) {
        try {
            var constraints = getConstraintViolations(loginRequest);
            if(!constraints.isEmpty()) {
                log.warn("Login constraints violated at: {}", constraints.stream().findFirst());
                return ServiceResultFactory.createErrorResult(ErrorMapFactory.fromSet(constraints));
            }
            UserProfile user = getUserProfile(loginRequest);
            Authentication authentication = getAuthentication(loginRequest, user);
            setAuthentication(authentication);
            setTimezoneOffset(user);
            sendJwtCookie(response, authentication);
            return ServiceResultFactory.createSuccessResult(Map.of());

        } catch (AuthenticationException e) {
            log.warn("Authentication exception: {}", e.getMessage());
            return ServiceResultFactory.createErrorResult(Map.of("auth", "Authentication failed"));

        } catch (UserNotFoundException e) {
            log.info("UserNotFoundException exception: {}", e.getMessage());
            return ServiceResultFactory.createErrorResult(Map.of("login", "User does not exist"));
        }
    }

    @Transactional
    public ServiceResult signup(@NonNull String timeZone,
                                @NonNull SignupRequest signupRequest) {
        var constraints = getConstraintViolations(signupRequest);
        if(!constraints.isEmpty()) {
            log.warn("Signup constraints violated at: {}", constraints.stream().findFirst());
            return ServiceResultFactory.createErrorResult(ErrorMapFactory.fromSet(constraints));
        }

        Optional<UserProfile> optionalUserProfile = getUserProfile(signupRequest);
        if(optionalUserProfile.isPresent()) {
            log.info("User with given username or email already exists.");
            return ServiceResultFactory.createErrorResult(Map.of("login", "User with given username or email already exists."));
        }

        String encodedPassword = getEncodedPassword(signupRequest);
        userProfileService.addUserProfile(signupRequest.login(), signupRequest.email(), encodedPassword, timeZone);
        return ServiceResultFactory.createSuccessResult(Map.of());
    }

    public ServiceResult logout(@NonNull HttpServletResponse httpServletResponse) {
        clearAuthentication();
        deleteJwtCookie(httpServletResponse);
        return ServiceResultFactory.createSuccessResult(Map.of());
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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.username(), loginRequest.password()));
        return authentication;
    }

    private UserProfile getUserProfile(LoginRequest loginRequest) {
        UserProfile user = userProfileService.findByUsername(loginRequest.login());
        return user;
    }
}
