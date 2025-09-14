package lule.dictionary.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.auth.data.exception.AuthServiceException;
import lule.dictionary.auth.data.localization.AuthText;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import lule.dictionary.language.service.Language;
import lule.dictionary.session.service.SessionHelper;
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
import lule.dictionary.validation.data.ValidationException;
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
    private final SessionHelper sessionHelper;

    public void login(@NonNull LoginRequest request,
                      @NonNull HttpServletResponse response,
                      @NonNull HttpSession session) {
        Language uiLanguage = sessionHelper.getUILanguage(session);
        try {
            validationService.validate(request, uiLanguage);
            AuthenticationData authResult = authenticateUser(request);
            setAuthenticationContext(SessionContext.of(authResult, response, session));
            log.info("User {} logged in successfully", request.login());
        } catch (ValidationException e) {
            log.warn("Validation failed for login request: {}", e.getViolation());
            throw new AuthServiceException(e.getViolation());
        } catch (UserNotFoundException e) {
            log.warn("User not found: {}", request.login());
            throw new AuthServiceException(Map.of("userNotFound", switch (uiLanguage) {
                case PL -> "Użytkownik nie został znaleziony";
                case EN -> "User not found";
                case IT -> "Utente non trovato";
                case NO -> "Bruker ikke funnet";
            }));
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user {}: {}", request.login(), e.getMessage());
            throw new AuthServiceException(Map.of("authenticationFailed", switch (uiLanguage) {
                case PL -> "Nieprawidłowe dane logowania";
                case EN -> "Invalid login credentials";
                case IT -> "Credenziali di accesso non valide";
                case NO -> "Ugyldige påloggingsdetaljer";
            }));
        }
    }

    @Transactional
    public void signup(@NonNull SignupRequest request, HttpSession httpSession) {
        Language uiLanguage = sessionHelper.getUILanguage(httpSession);
        try {
            validationService.validate(request, uiLanguage);
            userProfileService.findByUsernameOrEmail(request.login(), request.email())
                    .ifPresentOrElse(
                            user -> { throw new UserExistsException("User with given username or email already exists"); },
                            () -> userProfileService.addUserProfile(request)
                    );
            log.info("User {} signed up successfully", request.login());
        } catch (ValidationException e) {
            log.warn("Validation failed for signup request: {}", e.getViolation());
            throw new AuthServiceException(e.getViolation());
        } catch (UserExistsException e) {
            log.warn("User already exists: {}", request.login());
            throw new AuthServiceException(Map.of("userExists", switch (uiLanguage) {
                case PL -> "Użytkownik już istnieje";
                case EN -> "User already exists";
                case IT -> "L'utente esiste già";
                case NO -> "Brukeren finnes allerede";
            }));
        }
    }

    public void logout(@NonNull HttpServletResponse httpServletResponse) {
        String username = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "unknown";
        clearAuthentication();
        deleteJwtCookie(httpServletResponse);
        log.info("User {} logged out", username);
    }

    private AuthenticationData authenticateUser(AuthRequest loginData) throws UserNotFoundException, AuthenticationException {
        UserProfile user = getUserProfile(loginData);
        Authentication authentication = authenticate(user);
        return AuthenticationData.of(authentication, user);
    }

    private void setAuthenticationContext(SessionContext sessionContext) {
        setAuthentication(sessionContext);
        sendJwtCookie(sessionContext);
        updateTimezoneOffset(getUsername(sessionContext), TimeZoneOffsetContext.get());
        sessionContext.httpSession().setAttribute("isProfileOpen", false);
    }

    private void updateTimezoneOffset(String owner, String offset) {
        userProfileService.updateTimezoneOffset(owner, offset);
    }

    private void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
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
        return cookieService.createJwtCookie("jwt", jwtService.generateToken(username));
    }

    private void setAuthentication(SessionContext sessionContext) {
        Authentication authentication = sessionContext != null
                ? sessionContext.authenticationData().authentication()
                : null;
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication authenticate(UserProfile userProfile) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userProfile.getUsername(), userProfile.getPassword()));
    }

    private UserProfile getUserProfile(AuthRequest request) throws UserNotFoundException {
        return ((UserProfile) userProfileService.loadUserByUsername(request.login())).withPassword(request.password());
    }

    public Map<AuthText, String> getTextLocalization(HttpSession session) {
        Language uiLanguage = sessionHelper.getUILanguage(session);
        return authLocalizationService.getTextLocalization(uiLanguage);
    }
}