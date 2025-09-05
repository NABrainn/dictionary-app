package lule.dictionary.userProfiles.data;


import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder(toBuilder = true)
public record UserProfile(
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
        @NonNull
        Language userInterfaceLanguage,
        int wordsAddedToday,
        int dailyStreak,
        String offset) implements UserDetails {

    public static UserProfile copyOf(UserProfile userProfile, String password) {
        return UserProfile.builder()
                .username(userProfile.getUsername())
                .email(userProfile.email())
                .password(password)
                .sourceLanguage(userProfile.sourceLanguage())
                .targetLanguage(userProfile.targetLanguage())
                .wordsAddedToday(userProfile.wordsAddedToday())
                .dailyStreak(userProfile.dailyStreak())
                .userInterfaceLanguage(userProfile.userInterfaceLanguage())
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
