package lule.dictionary.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
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
            validateInputs(loginRequest);
            UserProfile user = getUserProfile(loginRequest);
            setAuthenticationContext(user.username(), loginRequest.password(), user.offset(), response);
            return ServiceResultFactory.createSuccessResult(Map.of());

        }
        catch (ConstraintViolationException e) {
            log.warn("ConstraintViolationException: {}", e.getMessage());
            return ServiceResultFactory.createErrorResult(ErrorMapFactory.fromSetWildcard(e.getConstraintViolations()));
        }

        catch (UserNotFoundException e) {
            log.info("UserNotFoundException exception: {}", e.getMessage());
            return ServiceResultFactory.createErrorResult(Map.of("login", "User does not exist"));
        }

        catch (AuthenticationException e) {
            log.warn("Authentication exception: {}", e.getMessage());
            return ServiceResultFactory.createErrorResult(Map.of("password", e.getMessage()));

        }
    }

    @Transactional
    public ServiceResult signup(@NonNull String timeZone,
                                @NonNull SignupRequest signupRequest) {
        try {
            validateInputs(signupRequest);
            getUserProfile(signupRequest);
            userProfileService.addUserProfile(signupRequest.login(), signupRequest.email(), getEncodedPassword(signupRequest), timeZone);
            return ServiceResultFactory.createSuccessResult(Map.of());
        }

        catch (ConstraintViolationException e) {
            log.info(e.getMessage());
            return ServiceResultFactory.createErrorResult(ErrorMapFactory.fromSetWildcard(e.getConstraintViolations()));
        }

        catch (UserExistsException e) {
            log.info(e.getMessage());
            return ServiceResultFactory.createErrorResult(Map.of("login", e.getMessage()));
        }
    }

    public ServiceResult logout(@NonNull HttpServletResponse httpServletResponse) {
        clearAuthentication();
        deleteJwtCookie(httpServletResponse);
        return ServiceResultFactory.createSuccessResult(Map.of());
    }

    private void setAuthenticationContext(String username,
                                          String password,
                                          String offset,
                                          HttpServletResponse response) throws AuthenticationException {
        Authentication authentication = authenticate(username, password);
        setAuthentication(authentication);
        setTimezoneOffset(offset);
        sendJwtCookie(response, authentication);
    }

    private void validateInputs(AuthRequest authRequest) throws ConstraintViolationException {
        var constraints = getConstraintViolations(authRequest);
        if(!constraints.isEmpty()) {
            throw new ConstraintViolationException(constraints);
        }
    }

    private void clearAuthentication() {
        setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    private String getEncodedPassword(SignupRequest signupRequest) {
        return bCryptPasswordEncoder.encode(signupRequest.password());
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
        return cookieService.createJwtCookie("jwt", jwtService.generateTokenPair(authentication));
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setTimezoneOffset(String offset) {
        TimeZoneOffsetContext.set(offset);
    }

    private Authentication authenticate(String login, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
    }

    private void getUserProfile(SignupRequest signupRequest) throws UserNotFoundException {
        if (userProfileService.findByUsernameOrEmail(signupRequest.login(), signupRequest.email()).isPresent())
            throw new UserExistsException("User with given username already exists");
    }

    private UserProfile getUserProfile(LoginRequest loginRequest) {
        return userProfileService.findByUsername(loginRequest.login());
    }
}
