package lule.dictionary.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.auth.data.exception.AuthServiceException;
import lule.dictionary.auth.data.localization.AuthError;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        var systemLanguage = Language.EN;
        var sanitizedLogin = patternService.removeSpecialCharacters(request.login());
        var errorMessages = authLocalizationService.errorLocalization(systemLanguage);

        var validationResult = validator.validate(
                Constraint.of("login", NotEmpty.of(sanitizedLogin), errorMessages.get(AuthError.EMPTY_LOGIN)),
                Constraint.of("login", Size.of(sanitizedLogin, 0, 50), errorMessages.get(AuthError.SIZE_LOGIN)),
                Constraint.of("password", NotEmpty.of(request.password()), errorMessages.get(AuthError.EMPTY_PASSWORD)),
                Constraint.of("password", Size.of(request.password(), 0, 500), errorMessages.get(AuthError.SIZE_PASSWORD))
        );

        return switch (validationResult) {
            case Ok<?> _ -> {
                try {
                    log.debug("Loading user profile for: {}", sanitizedLogin);
                    var user = ((UserProfile) userProfileService.loadUserByUsername(sanitizedLogin)).withPassword(request.password());

                    log.debug("Authenticating user: {}", sanitizedLogin);
                    securityContextService.authenticateAndSetContext(user, authenticationManager);
                    var jwtToken = jwtService.generateToken(user.getUsername());

                    log.info("Generated JWT for user: {}", sanitizedLogin);
                    var jwtCookie = cookieService.createJwtCookie("jwt", jwtToken);
                    userProfileService.updateTimezoneOffset(user.getUsername(), TimeZoneOffsetContext.get());
                    response.addCookie(jwtCookie);

                    log.info("User {} logged in successfully, cookie set", sanitizedLogin);
                    yield Ok.empty();

                }

                catch (UserNotFoundException e) {
                    log.warn("User not found: {}", sanitizedLogin, e);
                    yield Err.of(new AuthServiceException(Map.of("userNotFound", errorMessages.get(AuthError.USER_NOT_FOUND))));
                }

                catch (Exception e) {
                    log.error("Unexpected error during login for user: {}", sanitizedLogin, e);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            case Err<?> v -> switch (v.throwable()) {
                case ValidationException validationException -> {
                    log.warn("Validation failed for login request: {}", validationException.getViolations());
                    yield Err.of(new AuthServiceException(validationException.getViolations()));
                }

                default -> {
                    log.error("Unexpected error in login validation: {}", v.throwable(), v.throwable());
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            };
        };
    }

    @Transactional
    public Result<?> signup(@NonNull SignupRequest request) {
        log.info("Processing signup request for user: {}", request.login());
        var sanitizedLogin = patternService.removeSpecialCharacters(request.login()).trim();

        log.debug("Sanitized login: {}", sanitizedLogin);
        var systemLanguage = Language.EN;
        var errorMessages = authLocalizationService.errorLocalization(systemLanguage);

        var validationResult = validator.validate(
                Constraint.of("login", NotEmpty.of(sanitizedLogin), errorMessages.get(AuthError.EMPTY_LOGIN)),
                Constraint.of("login", Size.of(sanitizedLogin, 0, 50), errorMessages.get(AuthError.SIZE_LOGIN)),
                Constraint.of("email", NotEmpty.of(request.email()), errorMessages.get(AuthError.EMPTY_EMAIL)),
                Constraint.of("email", Size.of(request.email(), 0, 200), errorMessages.get(AuthError.SIZE_EMAIL)),
                Constraint.of("email", Email.of(request.email()), errorMessages.get(AuthError.INVALID_EMAIL)),
                Constraint.of("password", NotEmpty.of(request.password()), errorMessages.get(AuthError.EMPTY_PASSWORD)),
                Constraint.of("password", Size.of(request.password(), 0, 500), errorMessages.get(AuthError.SIZE_PASSWORD))
        );

        return switch (validationResult) {
            case Ok<?> _ -> {
                log.debug("Checking if user exists: login={}, email={}", sanitizedLogin, request.email());
                var userProfileResult = userProfileService.loadByUsernameOrEmail(request.login(), request.email(), errorMessages.get(AuthError.USER_EXISTS));
                yield switch (userProfileResult) {
                    case Err<UserProfile> v -> {
                        log.warn("User already exists: login={}, email={}", sanitizedLogin, request.email());
                        yield Err.of(v.throwable());
                    }
                    case Ok<UserProfile> _ -> {
                        log.debug("Creating new user profile for: {}", sanitizedLogin);
                        userProfileService.addUserProfile(request);
                        yield Ok.empty();
                    }
                };
            }

            case Err<?> v -> switch (v.throwable()) {
                case ValidationException validationException -> {
                    log.warn("Validation failed for signup request: {}", validationException.getViolations());
                    yield Err.of(new AuthServiceException(validationException.getViolations()));
                }

                default -> {
                    log.error("Unexpected error in signup validation: {}", v.throwable(), v.throwable());
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            };
        };
    }

    public Map<AuthText, String> getTextLocalization() {
        var systemLanguage = Language.EN;
        log.debug("Fetching text localization for language: {}", systemLanguage);
        return authLocalizationService.getTextLocalization(systemLanguage);
    }
}