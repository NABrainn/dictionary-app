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
import lule.dictionary.entity.application.interfaces.userProfile.UserProfileFactory;
import lule.dictionary.service.auth.dto.request.AuthRequest;
import lule.dictionary.service.auth.dto.authenticationContext.SessionContext;
import lule.dictionary.service.auth.dto.request.imp.LoginRequest;
import lule.dictionary.service.auth.dto.request.imp.SignupRequest;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.auth.dto.authenticationContext.SessionContextFactory;
import lule.dictionary.service.auth.dto.authenticationResult.AuthenticationResult;
import lule.dictionary.service.auth.dto.authenticationResult.AuthenticationResultFactory;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserProfileService userProfileService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final Validator validator;
    private final UserProfileFactory userProfileFactory;
    private final SessionContextFactory sessionContextFactory;
    private final ServiceResultFactory serviceResultFactory;
    private final AuthenticationResultFactory authenticationResultFactory;


    public ServiceResult login(@NonNull LoginRequest loginData,
                               @NonNull HttpServletResponse response) {
        try {
            return processLoginRequest(loginData, response);
        }

        catch (ConstraintViolationException e) {
            log.warn("ConstraintViolationException: {}", e.getMessage());
            return handleException(ErrorMapFactory.fromSetWildcard(e.getConstraintViolations()));
        }

        catch (UserNotFoundException e) {
            log.info("UserNotFoundException exception: {}", e.getMessage());
            return handleException(Map.of("login", "User does not exist"));
        }

        catch (AuthenticationException e) {
            log.warn("Authentication exception: {}", e.getMessage());
            return handleException(Map.of("password", e.getMessage()));

        }
    }

    @Transactional
    public ServiceResult signup(@NonNull SignupRequest signupRequest) {
        try {
            return processSignupRequest(signupRequest);
        }

        catch (ConstraintViolationException e) {
            log.info(e.getMessage());
            return handleException(ErrorMapFactory.fromSetWildcard(e.getConstraintViolations()));
        }

        catch (UserExistsException e) {
            log.info(e.getMessage());
            return handleException(Map.of("login", e.getMessage()));
        }
    }

    public ServiceResult logout(@NonNull HttpServletResponse httpServletResponse) {
        clearAuthentication();
        deleteJwtCookie(httpServletResponse);
        return serviceResultFactory.createSuccessResult(Map.of());
    }

    private ServiceResult processLoginRequest(LoginRequest loginData, HttpServletResponse response) throws ConstraintViolationException {
        AuthRequest validLoginData = validate(loginData);
        AuthenticationResult authResult = authenticateUser(validLoginData);
        setAuthenticationContext(sessionContextFactory.of(authResult, response));
        return serviceResultFactory.createSuccessResult(Map.of());
    }

    private ServiceResult handleException(Map<String, String> stringStringMap) {
        return serviceResultFactory.createErrorResult(stringStringMap);
    }

    private ServiceResult processSignupRequest(SignupRequest signupData) {
        AuthRequest validSignupData = validate(signupData);
        getUserProfile(validSignupData);
        userProfileService.addUserProfile((SignupRequest) validSignupData);
        return serviceResultFactory.createSuccessResult(Map.of());
    }


    private AuthenticationResult authenticateUser(AuthRequest loginData) {
        UserProfile user = getUserProfile(loginData);
        Authentication authentication = authenticate(user);
        return authenticationResultFactory.of(authentication, user);
    }

    private void setAuthenticationContext(SessionContext sessionContext) throws AuthenticationException {
        setAuthentication(sessionContext.authenticationResult().authentication());
        setTimezoneOffset(sessionContext.authenticationResult().userProfile().offset());
        sendJwtCookie(sessionContext.authenticationResult().userProfile().username(), sessionContext.response());
    }

    private AuthRequest validate(AuthRequest authRequest) throws ConstraintViolationException {
        var constraints = getConstraintViolations(authRequest);
        ifViolatedThrow(constraints);
        return authRequest;
    }

    private void ifViolatedThrow(Set<ConstraintViolation<AuthRequest>> constraints) {
        if(!constraints.isEmpty()) {
            throw new ConstraintViolationException(constraints);
        }
    }

    private void clearAuthentication() {
        setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    private void deleteJwtCookie(HttpServletResponse response) {
        Cookie cookie = cookieService.deleteJwtCookie("jwt");
        response.addCookie(cookie);
    }

    private Set<ConstraintViolation<@NonNull AuthRequest>> getConstraintViolations(AuthRequest authRequest) {
        return validator.validate(authRequest);
    }


    private void sendJwtCookie(String username, HttpServletResponse response) {
        Cookie jwtCookie = createJwtCookie(username);
        attachToResponse(jwtCookie, response);
    }

    private void attachToResponse(Cookie jwtCookie, HttpServletResponse response) {
        response.addCookie(jwtCookie);
    }

    private Cookie createJwtCookie(String username) {
        return cookieService.createJwtCookie("jwt", jwtService.generateTokenPair(username));
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setTimezoneOffset(String offset) {
        TimeZoneOffsetContext.set(offset);
    }

    private Authentication authenticate(UserProfile userProfile) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userProfile.username(), userProfile.password()));
    }

    private void getUserProfile(SignupRequest signupRequest) throws UserNotFoundException {
        if (userProfileService.findByUsernameOrEmail(signupRequest.login(), signupRequest.email()).isPresent())
            throw new UserExistsException("User with given username already exists");
    }

    private UserProfile getUserProfile(AuthRequest loginRequest) {
        return userProfileFactory.withNewPassword(userProfileService.findByUsername(loginRequest.login()), loginRequest.password());
    }
}
