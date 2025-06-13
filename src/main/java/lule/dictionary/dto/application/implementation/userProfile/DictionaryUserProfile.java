package lule.dictionary.dto.application.implementation.userProfile;


import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
public record DictionaryUserProfile(
        @NonNull
        UserProfileCredentials userProfileCredentials,
        UserProfileSettings userProfileSettings) implements UserProfile, UserDetails {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public String getPassword() {
                return userProfileCredentials.password();
        }

        @Override
        public String getUsername() {
                return userProfileCredentials.username();
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
