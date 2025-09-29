package lule.dictionary.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.auth.data.exception.AuthServiceException;
import lule.dictionary.auth.data.localization.AuthText;
import lule.dictionary.security.data.TimeZoneOffsetContext;
import lule.dictionary.language.service.Language;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.auth.data.request.LoginRequest;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.cookie.service.CookieService;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import lule.dictionary.jwt.service.JwtService;
import lule.dictionary.userProfiles.service.UserProfileService;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.ValidationException;
import lule.dictionary.validation.data.rule.Email;
import lule.dictionary.validation.data.rule.NotEmpty;
import lule.dictionary.validation.data.rule.Size;
import lule.dictionary.validation.service.Validator;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final SecurityContextService securityContextService;

    public Result<?> login(@NonNull LoginRequest request, @NonNull HttpServletResponse response) {
        log.info("Processing login request for user: {}", request.login());
        String sanitizedLogin = patternService.removeSpecialCharacters(request.login());
        Result<?> result = validator.validate(
                Constraint.of("login", NotEmpty.of(sanitizedLogin), switch (Language.EN) {
                    case PL -> "Nazwa użytkownika nie może być pusta";
                    case EN -> "Username cannot be empty";
                    case IT -> "Il nome utente non può essere vuoto";
                    case NO -> "Brukernavnet kan ikke være tomt";
                }),
                Constraint.of("login", Size.of(sanitizedLogin, 0, 50), switch (Language.EN) {
                    case PL -> "Nazwa użytkownika nie może być dłuższa niż 50 znaków";
                    case EN -> "Username cannot be longer than 50 characters";
                    case IT -> "Il nome utente non può essere più lungo di 50 caratteri";
                    case NO -> "Brukernavnet kan ikke være lenger enn 50 tegn";
                }),
                Constraint.of("password", NotEmpty.of(request.password()), switch (Language.EN) {
                    case PL -> "Hasło nie może być puste";
                    case EN -> "Password cannot be empty";
                    case IT -> "La password non può essere vuota";
                    case NO -> "Passordet kan ikke være tomt";
                }),
                Constraint.of("password", Size.of(request.password(), 0, 500), switch (Language.EN) {
                    case PL -> "Hasło nie może być dłuższe niż 500 znaków";
                    case EN -> "Password cannot be longer than 500 characters";
                    case IT -> "La password non può essere più lunga di 500 caratteri";
                    case NO -> "Passordet kan ikke være lenger enn 500 tegn";
                })
        );

        return switch (result) {
            case Ok<?> ignored1 -> {
                try {
                    log.debug("Loading user profile for: {}", sanitizedLogin);
                    UserProfile user = ((UserProfile) userProfileService.loadUserByUsername(sanitizedLogin))
                            .withPassword(request.password());
                    log.debug("Authenticating user: {}", sanitizedLogin);
                    securityContextService.authenticateAndSetContext(user, authenticationManager);
                    String token = jwtService.generateToken(user.getUsername());
                    log.info("Generated JWT for user: {}", sanitizedLogin);
                    Cookie jwtCookie = cookieService.createJwtCookie("jwt", token);
                    userProfileService.updateTimezoneOffset(user.getUsername(), TimeZoneOffsetContext.get());
                    response.addCookie(jwtCookie);
                    log.info("User {} logged in successfully, cookie set", sanitizedLogin);
                    yield Ok.empty();
                } catch (UserNotFoundException e) {
                    log.warn("User not found: {}", sanitizedLogin, e);
                    yield Err.of(new AuthServiceException(Map.of("userNotFound", switch (Language.EN) {
                        case PL -> "Użytkownik nie został znaleziony";
                        case EN -> "User not found";
                        case IT -> "Utente non trovato";
                        case NO -> "Bruker ikke funnet";
                    })));
                } catch (Exception e) {
                    log.error("Unexpected error during login for user: {}", sanitizedLogin, e);
                    throw e;
                }
            }
            case Err<?> v -> switch (v.throwable()) {
                case ValidationException validationException -> {
                    log.warn("Validation failed for login request: {}", validationException.getViolations());
                    yield Err.of(new AuthServiceException(validationException.getViolations()));
                }
                default -> {
                    log.error("Unexpected error in login validation: {}", v.throwable(), v.throwable());
                    throw new IllegalStateException("Unexpected value: " + v.throwable());
                }
            };
        };
    }

    @Transactional
    public Result<?> signup(@NonNull SignupRequest request) {
        log.info("Processing signup request for user: {}", request.login());
        String sanitizedLogin = patternService.removeSpecialCharacters(request.login()).trim();
        log.debug("Sanitized login: {}", sanitizedLogin);

        Result<?> result = validator.validate(
                Constraint.of("login", NotEmpty.of(sanitizedLogin), switch (Language.EN) {
                    case PL -> "Nazwa użytkownika nie może być pusta";
                    case EN -> "Username cannot be empty";
                    case IT -> "Il nome utente non può essere vuoto";
                    case NO -> "Brukernavnet kan ikke være tomt";
                }),
                Constraint.of("login", Size.of(sanitizedLogin, 0, 50), switch (Language.EN) {
                    case PL -> "Nazwa użytkownika nie może być dłuższa niż 50 znaków";
                    case EN -> "Username cannot be longer than 50 characters";
                    case IT -> "Il nome utente non può essere più lungo di 50 caratteri";
                    case NO -> "Brukernavnet kan ikke være lenger enn 50 tegn";
                }),
                Constraint.of("email", NotEmpty.of(request.email()), switch (Language.EN) {
                    case PL -> "Adres e-mail nie może być pusty";
                    case EN -> "Email address cannot be empty";
                    case IT -> "L'indirizzo email non può essere vuoto";
                    case NO -> "E-postadressen kan ikke være tom";
                }),
                Constraint.of("email", Size.of(request.email(), 0, 200), switch (Language.EN) {
                    case PL -> "Adres e-mail nie może być dłuższy niż 200 znaków";
                    case EN -> "Email address cannot be longer than 200 characters";
                    case IT -> "L'indirizzo email non può essere più lungo di 200 caratteri";
                    case NO -> "E-postadressen kan ikke være lenger enn 200 tegn";
                }),
                Constraint.of("email", Email.of(request.email()), switch (Language.EN) {
                    case PL -> "Nieprawidłowy format adresu e-mail";
                    case EN -> "Invalid email address format";
                    case IT -> "Formato dell'indirizzo email non valido";
                    case NO -> "Ugyldig format for e-postadresse";
                }),
                Constraint.of("password", NotEmpty.of(request.password()), switch (Language.EN) {
                    case PL -> "Hasło nie może być puste";
                    case EN -> "Password cannot be empty";
                    case IT -> "La password non può essere vuota";
                    case NO -> "Passordet kan ikke være tomt";
                }),
                Constraint.of("password", Size.of(request.password(), 0, 500), switch (Language.EN) {
                    case PL -> "Hasło nie może być dłuższe niż 500 znaków";
                    case EN -> "Password cannot be longer than 500 characters";
                    case IT -> "La password non può essere più lunga di 500 caratteri";
                    case NO -> "Passordet kan ikke være lenger enn 500 tegn";
                })
        );

        return switch (result) {
            case Ok<?> ignored -> {
                log.debug("Checking if user exists: login={}, email={}", sanitizedLogin, request.email());
                userProfileService.loadByUsernameOrEmail(request.login(), request.email())
                        .ifPresentOrElse(
                                user -> {
                                    log.warn("User already exists: login={}, email={}", sanitizedLogin, request.email());
                                    Err.of(new AuthServiceException(Map.of("userExists", switch (Language.EN) {
                                        case PL -> "Użytkownik już istnieje";
                                        case EN -> "User already exists";
                                        case IT -> "L'utente esiste già";
                                        case NO -> "Brukeren finnes allerede";
                                    })));
                                },
                                () -> {
                                    log.debug("Creating new user profile for: {}", sanitizedLogin);
                                    userProfileService.addUserProfile(request);
                                });
                log.info("User {} signed up successfully", sanitizedLogin);
                yield Ok.empty();
            }
            case Err<?> v -> switch (v.throwable()) {
                case ValidationException validationException -> {
                    log.warn("Validation failed for signup request: {}", validationException.getViolations());
                    yield Err.of(new AuthServiceException(validationException.getViolations()));
                }
                default -> {
                    log.error("Unexpected error in signup validation: {}", v.throwable(), v.throwable());
                    throw new IllegalStateException("Unexpected value: " + v.throwable());
                }
            };
        };
    }

    public Map<AuthText, String> getTextLocalization() {
        log.debug("Fetching text localization for language: {}", Language.EN);
        return authLocalizationService.getTextLocalization(Language.EN);
    }
}