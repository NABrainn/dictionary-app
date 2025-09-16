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
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.auth.data.request.LoginRequest;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.cookie.service.CookieService;
import lule.dictionary.userProfiles.service.exception.UserExistsException;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import lule.dictionary.jwt.service.JwtService;
import lule.dictionary.userProfiles.service.UserProfileService;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.ValidationException;
import lule.dictionary.validation.service.Validator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserProfileService userProfileService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final Validator validator;
    private final AuthLocalizationService authLocalizationService;
    private final PatternService patternService;
    private final SessionHelper sessionHelper;

    public void login(@NonNull LoginRequest request,
                      @NonNull HttpServletResponse response,
                      @NonNull HttpSession session) {
        Language uiLanguage = sessionHelper.getUILanguage(session);
        try {
            String sanitizedLogin = patternService.removeSpecialCharacters(request.login()).trim();
            validator.validate(List.of(
                    Constraint.define("login", sanitizedLogin::isBlank, switch(uiLanguage){
                        case PL -> "Nazwa użytkownika nie może być pusta";
                        case EN -> "Username cannot be empty";
                        case IT -> "Il nome utente non può essere vuoto";
                        case NO -> "Brukernavnet kan ikke være tomt";
                    }),
                    Constraint.define("login", () -> sanitizedLogin.length() > 50, switch(uiLanguage){
                        case PL -> "Nazwa użytkownika nie może być dłuższa niż 50 znaków";
                        case EN -> "Username cannot be longer than 50 characters";
                        case IT -> "Il nome utente non può essere più lungo di 50 caratteri";
                        case NO -> "Brukernavnet kan ikke være lenger enn 50 tegn";
                    }),
                    Constraint.define("password", () -> request.password().isBlank(), switch(uiLanguage){
                        case PL -> "Hasło nie może być puste";
                        case EN -> "Password cannot be empty";
                        case IT -> "La password non può essere vuota";
                        case NO -> "Passordet kan ikke være tomt";
                    }),
                    Constraint.define("password", () -> request.password().length() > 500, switch(uiLanguage){
                        case PL -> "Hasło nie może być dłuższe niż 500 znaków";
                        case EN -> "Password cannot be longer than 500 characters";
                        case IT -> "La password non può essere più lunga di 500 caratteri";
                        case NO -> "Passordet kan ikke være lenger enn 500 tegn";
                    })
            ));
            UserProfile user = ((UserProfile) userProfileService.loadUserByUsername(sanitizedLogin)).withPassword(request.password());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(user.getUsername());
            Cookie jwtCookie = cookieService.createJwtCookie("jwt", token);
            userProfileService.updateTimezoneOffset(user.getUsername(), TimeZoneOffsetContext.get());
            response.addCookie(jwtCookie);
            session.setAttribute("isProfileOpen", false);
            log.info("User {} logged in successfully", request.login());

        } catch (ValidationException e) {
            log.warn("Validation failed for login request: {}", e.getViolations());
            throw new AuthServiceException(e.getViolations());

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
    public void signup(@NonNull SignupRequest request,
                       @NonNull HttpSession httpSession) {
        Language uiLanguage = sessionHelper.getUILanguage(httpSession);
        try {
            String sanitizedLogin = patternService.removeSpecialCharacters(request.login()).trim();
            validator.validate(List.of(
                    Constraint.define("login", sanitizedLogin::isBlank, switch(uiLanguage){
                        case PL -> "Nazwa użytkownika nie może być pusta";
                        case EN -> "Username cannot be empty";
                        case IT -> "Il nome utente non può essere vuoto";
                        case NO -> "Brukernavnet kan ikke være tomt";
                    }),
                    Constraint.define("login", () -> sanitizedLogin.length() > 50, switch(uiLanguage){
                        case PL -> "Nazwa użytkownika nie może być dłuższa niż 50 znaków";
                        case EN -> "Username cannot be longer than 50 characters";
                        case IT -> "Il nome utente non può essere più lungo di 50 caratteri";
                        case NO -> "Brukernavnet kan ikke være lenger enn 50 tegn";
                    }),
                    Constraint.define("email", request.email()::isBlank, switch(uiLanguage){
                        case PL -> "Adres e-mail nie może być pusty";
                        case EN -> "Email address cannot be empty";
                        case IT -> "L'indirizzo email non può essere vuoto";
                        case NO -> "E-postadressen kan ikke være tom";
                    }),
                    Constraint.define("email", () -> request.email().length() > 200, switch(uiLanguage){
                        case PL -> "Adres e-mail nie może być dłuższy niż 200 znaków";
                        case EN -> "Email address cannot be longer than 200 characters";
                        case IT -> "L'indirizzo email non può essere più lungo di 200 caratteri";
                        case NO -> "E-postadressen kan ikke være lenger enn 200 tegn";
                    }),
                    Constraint.define("email", () -> !patternService.isValidEmail(request.email()), switch(uiLanguage){
                        case PL -> "Nieprawidłowy format adresu e-mail";
                        case EN -> "Invalid email address format";
                        case IT -> "Formato dell'indirizzo email non valido";
                        case NO -> "Ugyldig format for e-postadresse";
                    }),
                    Constraint.define("password", () -> request.password().isBlank(), switch(uiLanguage){
                        case PL -> "Hasło nie może być puste";
                        case EN -> "Password cannot be empty";
                        case IT -> "La password non può essere vuota";
                        case NO -> "Passordet kan ikke være tomt";
                    }),
                    Constraint.define("password", () -> request.password().length() > 500, switch(uiLanguage){
                        case PL -> "Hasło nie może być dłuższe niż 500 znaków";
                        case EN -> "Password cannot be longer than 500 characters";
                        case IT -> "La password non può essere più lunga di 500 caratteri";
                        case NO -> "Passordet kan ikke være lenger enn 500 tegn";
                    })
            ));
            userProfileService.findByUsernameOrEmail(request.login(), request.email())
                    .ifPresentOrElse(
                            user -> { throw new UserExistsException("User with given username or email already exists"); },
                            () -> userProfileService.addUserProfile(request)
                    );
            log.info("User {} signed up successfully", request.login());

        } catch (ValidationException e) {
            log.warn("Validation failed for signup request: {}", e.getViolations());
            throw new AuthServiceException(e.getViolations());

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

    public void logout(@NonNull HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "unknown";
        SecurityContextHolder.getContext().setAuthentication(null);
        Cookie cookie = cookieService.deleteJwtCookie("jwt");
        response.addCookie(cookie);
        log.info("User {} logged out", username);
    }

    public Map<AuthText, String> getTextLocalization(@NonNull HttpSession session) {
        Language uiLanguage = sessionHelper.getUILanguage(session);
        return authLocalizationService.getTextLocalization(uiLanguage);
    }
}