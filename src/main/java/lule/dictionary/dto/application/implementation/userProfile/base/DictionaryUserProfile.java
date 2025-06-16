package lule.dictionary.dto.application.implementation.userProfile.base;


import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.enumeration.Language;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder(toBuilder = true)
public record DictionaryUserProfile(
        @NonNull
        String username,
        @NonNull
        String email,
        @NonNull
        String password,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage) implements UserProfile, UserDetails {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public String getPassword() {
                return password();
        }

        @Override
        public String getUsername() {
                return username();
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
