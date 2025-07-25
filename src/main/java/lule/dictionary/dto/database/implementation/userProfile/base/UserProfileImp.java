package lule.dictionary.dto.database.implementation.userProfile.base;


import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.language.Language;
import lule.dictionary.functionalInterface.validation.EmptyValidator;
import lule.dictionary.functionalInterface.validation.LengthValidator;
import lule.dictionary.functionalInterface.validation.PatternValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Builder(toBuilder = true)
public record UserProfileImp(
        @NonNull
        String username,
        @NonNull
        String email,
        @NonNull
        String password,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        int wordsAddedToday,
        int dailyStreak,
        String offset) implements UserProfile {

        public UserProfileImp {
                EmptyValidator emptyValidator = (String... fields) -> {
                        for(var field : fields) {
                                if(field.isEmpty()) throw new IllegalArgumentException("Field cannot be empty");
                        }
                };

                LengthValidator maxLengthValidator = (int maxLength, String field) -> {
                        if(field.length() > maxLength) throw new IllegalArgumentException("Field cannot be longer than " + maxLength + " characters");
                };

                LengthValidator minLengthValidator = (int minLength, String field) -> {
                        if(field.length() < minLength) throw new IllegalArgumentException("Field cannot be shorter than " + minLength + " characters");
                };

                PatternValidator patternValidator = (Pattern pattern, String field) -> {
                        if(pattern.matcher(field).find()) throw new IllegalArgumentException("Field " + field + " contains invalid characters");
                };

                emptyValidator.validate(username, email, password);

                maxLengthValidator.validate(50, username);
                maxLengthValidator.validate(100, email);
                maxLengthValidator.validate(500, password);

                minLengthValidator.validate(8, username);
                minLengthValidator.validate(8, password);

                final Pattern NO_SPECIAL_CHARS = Pattern.compile("[%&/^!<>@#$'\"*;`:=\\-_+.,(){}\\[\\]?\\\\]");
                final Pattern INVALID_EMAIL = Pattern.compile("[^A-Za-z0-9+_.@-]");
                patternValidator.validate(NO_SPECIAL_CHARS, username);
                patternValidator.validate(INVALID_EMAIL, email);
        }

    public static UserProfile withNewPassword(UserProfile userProfile, String password) {
        return UserProfileImp.builder()
                .username(userProfile.username())
                .email(userProfile.email())
                .password(password)
                .sourceLanguage(userProfile.sourceLanguage())
                .targetLanguage(userProfile.targetLanguage())
                .wordsAddedToday(userProfile.wordsAddedToday())
                .dailyStreak(userProfile.dailyStreak())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
            return password;
    }

    @Override
    public String getUsername() {
            return username;
    }

    @Override
    public boolean isAccountNonExpired() {
            return true;
    }

    @Override
    public boolean isAccountNonLocked() {
            return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
            return true;
    }

    @Override
    public boolean isEnabled() {
            return true;
    }
}
