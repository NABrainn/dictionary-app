package lule.dictionary.dto;


import lombok.NonNull;
import lule.dictionary.functionalInterface.EmptyValidator;
import lule.dictionary.functionalInterface.LengthValidator;
import lule.dictionary.functionalInterface.PatternValidator;

import java.util.Arrays;
import java.util.regex.Pattern;

public record UserProfile(
        @NonNull
        String username,
        @NonNull
        String email,
        @NonNull
        String password) {
    public UserProfile {
        final Pattern INVALID_SPECIAL_DIGITS = Pattern.compile("[%&/^!<>@#$'\"*;`:=\\-_+.,(){}\\[\\]?\\\\1234567890]");
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

        patternValidator.validate(INVALID_SPECIAL_DIGITS, username);
        emailValidator.validate(VALID_EMAIL, email);
        patternValidator.validate(INVALID_SPECIAL_DIGITS, password);

        minLengthValidator.validate(8, password);

        maxLengthValidator.validate(50, username);
        maxLengthValidator.validate(50, email);
    }
}
