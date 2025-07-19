package lule.dictionary.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import lule.dictionary.dto.database.implementation.userProfile.base.UserProfileImp;
import lule.dictionary.service.auth.dto.request.AuthRequest;
import lule.dictionary.service.auth.dto.authenticationContext.SessionContext;
import lule.dictionary.service.auth.dto.request.imp.LoginRequest;
import lule.dictionary.service.auth.dto.request.imp.SignupRequest;
import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.auth.dto.authenticationResult.AuthenticationData;
import lule.dictionary.service.cookie.CookieService;
import lule.dictionary.dto.application.result.ServiceResultImp;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.userProfile.exception.UserExistsException;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import lule.dictionary.service.validation.ValidationService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserProfileService userProfileService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final ValidationService validationService;


    public ServiceResult<?> login(@NonNull LoginRequest loginData,
                                  @NonNull HttpServletResponse response) {
        try {
            return processLoginRequest(loginData, response);
        }

        catch (ConstraintViolationException e) {
            log.warn("ConstraintViolationException: {}", e.getMessage());
            return handleException(ErrorMapFactory.fromViolations(e.getConstraintViolations()));
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
    public ServiceResult<?> signup(@NonNull SignupRequest signupRequest) {
        try {
            return processSignupRequest(signupRequest);
        }

        catch (ConstraintViolationException e) {
            log.info(e.getMessage());
            return handleException(ErrorMapFactory.fromViolations(e.getConstraintViolations()));
        }

        catch (UserExistsException e) {
            log.info(e.getMessage());
            return handleException(Map.of("login", e.getMessage()));
        }
    }

    public ServiceResult<?> logout(@NonNull HttpServletResponse httpServletResponse) {
        clearAuthentication();
        deleteJwtCookie(httpServletResponse);
        return ServiceResultImp.successEmpty(Map.of());
    }

    private ServiceResult<?> processLoginRequest(LoginRequest loginData, HttpServletResponse response) throws ConstraintViolationException, UserNotFoundException {
        validate(loginData);
        AuthenticationData authResult = authenticateUser(loginData);
        setAuthenticationContext(SessionContext.of(authResult, response));
        return ServiceResultImp.successEmpty(Map.of());
    }

    private ServiceResult<?> handleException(Map<String, String> errorMessages) {
        return ServiceResultImp.errorEmpty(errorMessages);
    }

    private ServiceResult<?> processSignupRequest(SignupRequest signupData) throws ConstraintViolationException, UserExistsException {
        validate(signupData);
        checkIfUserExists(signupData);
        addUser(signupData);
        return ServiceResultImp.successEmpty(Map.of());
    }

    private void addUser(SignupRequest signupData) {
        userProfileService.addUserProfile(signupData);
    }


    private AuthenticationData authenticateUser(AuthRequest loginData) throws UserNotFoundException {
        UserProfile user = getUserProfile(loginData);
        Authentication authentication = authenticate(user);
        return AuthenticationData.of(authentication, user);
    }

    private void setAuthenticationContext(SessionContext sessionContext) throws AuthenticationException {
        setAuthentication(sessionContext);
        sendJwtCookie(sessionContext);
        updateTimezoneOffset(getUsername(sessionContext), TimeZoneOffsetContext.get());
    }

    private void updateTimezoneOffset(String owner, String offset) {
        userProfileService.updateTimezoneOffset(owner, offset);
    }

    private void validate(AuthRequest authRequest) throws ConstraintViolationException {
        validationService.validate(authRequest);
    }

    private void clearAuthentication() {
        setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    private void deleteJwtCookie(HttpServletResponse response) {
        Cookie cookie = cookieService.deleteJwtCookie("jwt");
        response.addCookie(cookie);
    }

    private void sendJwtCookie(SessionContext sessionContext) {
        String username = getUsername(sessionContext);
        Cookie jwtCookie = createJwtCookie(username);
        addToResponse(jwtCookie, sessionContext.response());
    }

    private String getUsername(SessionContext sessionContext) {
        return sessionContext.authenticationData().userProfile().username();
    }

    private void addToResponse(Cookie jwtCookie, HttpServletResponse response) {
        response.addCookie(jwtCookie);
    }

    private Cookie createJwtCookie(String username) {
        return cookieService.createJwtCookie("jwt", jwtService.generateTokenPair(username));
    }

    private void setAuthentication(SessionContext sessionContext) {
        if(sessionContext != null) {
            Authentication authentication = sessionContext.authenticationData().authentication();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private Authentication authenticate(UserProfile userProfile) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userProfile.username(), userProfile.password()));
    }

    private void checkIfUserExists(SignupRequest signupRequest) throws UserNotFoundException {
        if (userProfileService.findByUsernameOrEmail(signupRequest.login(), signupRequest.email()).isPresent())
            throw new UserExistsException("User with given username already exists");
    }

    private UserProfile getUserProfile(AuthRequest loginRequest) throws UserNotFoundException {
        return UserProfileImp.withNewPassword(userProfileService.getUserProfile(loginRequest.login()), loginRequest.password());
    }
}
