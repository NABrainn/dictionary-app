package lule.dictionary.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import lule.dictionary.service.userProfile.exception.UserExistsException;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import lule.dictionary.service.validation.ValidationService;
import lule.dictionary.service.jwt.JwtService;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserProfileService userProfileService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final ValidationService validationService;


    public void login(@NonNull LoginRequest loginData,
                      @NonNull HttpServletResponse response,
                      @NonNull HttpSession httpSession) {
        validationService.validate(loginData);
        AuthenticationData authResult = authenticateUser(loginData);
        setAuthenticationContext(SessionContext.of(authResult, response, httpSession));
    }

    @Transactional
    public void signup(@NonNull SignupRequest request) {
        validationService.validate(request);
        userProfileService.findByUsernameOrEmail(request.login(), request.email())
                .ifPresentOrElse(
                        user -> {
                            throw new UserExistsException("User with given username or email already exists");
                        },
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

    private UserProfile getUserProfile(AuthRequest loginRequest) throws UserNotFoundException {
        return UserProfileImp.withNewPassword(userProfileService.getUserProfile(loginRequest.login()), loginRequest.password());
    }
}
