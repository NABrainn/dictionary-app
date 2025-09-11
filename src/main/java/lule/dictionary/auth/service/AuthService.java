package lule.dictionary.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.auth.data.exception.EmailException;
import lule.dictionary.auth.data.exception.LoginException;
import lule.dictionary.auth.data.exception.PasswordException;
import lule.dictionary.auth.data.localization.AuthTextLocalization;
import lule.dictionary.auth.data.localization.group.EmailViolation;
import lule.dictionary.auth.data.localization.group.LoginViolation;
import lule.dictionary.auth.data.localization.group.PasswordViolation;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import lule.dictionary.language.service.Language;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.auth.data.request.AuthRequest;
import lule.dictionary.auth.data.SessionContext;
import lule.dictionary.auth.data.request.LoginRequest;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.auth.data.AuthenticationData;
import lule.dictionary.cookie.service.CookieService;
import lule.dictionary.userProfiles.service.exception.UserExistsException;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import lule.dictionary.jwt.service.JwtService;
import lule.dictionary.userProfiles.service.UserProfileService;
import lule.dictionary.validation.service.ValidationService;
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
    private final AuthLocalizationService authLocalizationService;


    public void login(@NonNull LoginRequest request,
                      @NonNull HttpServletResponse response,
                      @NonNull HttpSession httpSession) {
        validationService.validate(request, LoginViolation.class).ifPresent(violation -> { throw new LoginException(request.uiLanguage(), violation); });
        validationService.validate(request, PasswordViolation.class).ifPresent(violation -> { throw new PasswordException(request.uiLanguage(), violation); });
        AuthenticationData authResult = authenticateUser(request);
        setAuthenticationContext(SessionContext.of(authResult, response, httpSession));
    }

    @Transactional
    public void signup(@NonNull SignupRequest request) {
        validationService.validate(request, LoginViolation.class).ifPresent(violation -> { throw new LoginException(request.uiLanguage(), violation); });
        validationService.validate(request, EmailViolation.class).ifPresent(violation -> { throw new EmailException(request.uiLanguage(), violation); });
        validationService.validate(request, PasswordViolation.class).ifPresent(violation -> { throw new PasswordException(request.uiLanguage(), violation); });
        userProfileService.findByUsernameOrEmail(request.login(), request.email())
                .ifPresentOrElse(
                        user -> { throw new UserExistsException("User with given username or email already exists"); },
                        () -> userProfileService.addUserProfile(request)
                );
    }
    public void logout(@NonNull HttpServletResponse httpServletResponse) {
        clearAuthentication();
        deleteJwtCookie(httpServletResponse);
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
        sessionContext.httpSession().setAttribute("isProfileOpen", false);
    }

    private void updateTimezoneOffset(String owner, String offset) {
        userProfileService.updateTimezoneOffset(owner, offset);
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
        return sessionContext.authenticationData().userProfile().getUsername();
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
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userProfile.getUsername(), userProfile.getPassword()));
    }

    private UserProfile getUserProfile(AuthRequest loginRequest) throws UserNotFoundException {
        return userProfileService.findByLogin(loginRequest.login()).withPassword(loginRequest.password());
    }

    public Map<AuthTextLocalization, String> getTextLocalization(Language language) {
        return authLocalizationService.getTextLocalization(language);
    }
}
