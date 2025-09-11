package lule.dictionary.auth.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lule.dictionary.auth.data.localization.group.EmailViolation;
import lule.dictionary.auth.data.localization.group.LoginViolation;
import lule.dictionary.auth.data.localization.group.PasswordViolation;
import lule.dictionary.language.service.Language;

public record SignupRequest(
        @NotBlank(message = "LOGIN_CANNOT_BE_EMPTY", groups = LoginViolation.class)
        @Pattern(message = "LOGIN_MUST_CONTAIN_ONLY_LETTERS", regexp = "^[a-zA-Z]{8,50}$", groups = LoginViolation.class)
        @Size(message = "LOGIN_MUST_BE_BETWEEN_8_AND_50_CHARACTERS", min = 8, max = 50, groups = LoginViolation.class)
        String login,

        @NotBlank(message = "EMAIL_CANNOT_BE_EMPTY", groups = EmailViolation.class)
        @Size(min = 8, max = 100, message = "EMAIL_MUST_BE_BETWEEN_8_AND_150_CHARACTERS", groups = EmailViolation.class)
        @Email(message = "MUST_BE_VALID_EMAIL")
        String email,

        @NotBlank(message = "PASSWORD_CANNOT_BE_EMPTY", groups = PasswordViolation.class)
        @Size(message = "PASSWORD_MUST_BE_BETWEEN_8_AND_200_CHARACTERS", min = 8, max = 200, groups = PasswordViolation.class)
        String password,
        @NonNull Language uiLanguage) implements AuthRequest {
    public static SignupRequest of(String login, String email, String password, Language uiLanguage) {
        return new SignupRequest(login, email, password, uiLanguage);
    }
}
