package lule.dictionary.dto.application.implementation.userProfile;

import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.functionalInterface.validation.EmptyValidator;
import lule.dictionary.functionalInterface.validation.LengthValidator;
import lule.dictionary.functionalInterface.validation.PatternValidator;

import java.util.Arrays;
import java.util.regex.Pattern;

public record DictionaryUserProfileCredentials(
        @NonNull
        String username,
        @NonNull
        String email,
        @NonNull
        String password) implements UserProfileCredentials {
    public DictionaryUserProfileCredentials {
        final Pattern INVALID_SPECIAL_AND_DIGITS = Pattern.compile("[%&/^!<>@#$'\"*;`:=\\-_+.,(){}\\[\\]?\\\\1234567890]");
        final Pattern VALID_EMAIL = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        EmptyValidator emptyValidator = (String... fields) -> {
            Arrays.stream(fields).forEach(field -> {
                if(field.isEmpty()) throw new IllegalArgumentException("Field cannot be empty");
            });
        };

        PatternValidator patternValidator = (Pattern pattern, String field) -> {
            if(pattern.matcher(field).find()) {
                throw new IllegalArgumentException(field + " contains invalid characters");
            }
        };

        PatternValidator emailValidator = (Pattern pattern, String field) -> {
            if (!pattern.matcher(field).matches()) {
                throw new IllegalArgumentException(field + " is not a valid email address");
            }
        };

        LengthValidator minLengthValidator = (int length, String field) -> {
            if(field.length() < length) throw new IllegalArgumentException("Field cannot be shorter than " + length + " characters");
        };

        LengthValidator maxLengthValidator = (int length, String field) -> {
            if(field.length() > length) throw new IllegalArgumentException("Field cannot be longer than " + length + " characters");
        };
        emptyValidator.validate(username, email, password);

        username = username.trim();
        email = email.trim();
        password = password.trim();

        patternValidator.validate(INVALID_SPECIAL_AND_DIGITS, username);
        emailValidator.validate(VALID_EMAIL, email);

        minLengthValidator.validate(8, username);
        minLengthValidator.validate(8, password);

        maxLengthValidator.validate(50, username);
        maxLengthValidator.validate(50, email);
    }
}
