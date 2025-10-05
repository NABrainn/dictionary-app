package lule.dictionary.userProfiles.data;

import lombok.Builder;
import lombok.NonNull;
import lombok.With;
import lule.dictionary.language.service.Language;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@With
@Builder(toBuilder = true)
public record UserProfile(
        @NonNull String username,
        @NonNull String email,
        @NonNull String password,
        @NonNull Language sourceLanguage,
        @NonNull Language targetLanguage,
        @NonNull Language userInterfaceLanguage,
        boolean isNavbarOpen,
        int wordsAddedToday,
        int dailyStreak,
        String offset) implements UserDetails {

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
}
