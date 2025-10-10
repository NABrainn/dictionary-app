package lule.dictionary.userProfiles.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.userProfiles.data.repository.UserProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NavService {

    private final UserProfileRepository userProfileRepository;

    public boolean toggle(String item, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return item.equals("progressionPanel") && userProfileRepository.toggleNavbar(principal.getUsername());
    }

    public boolean isNavbarOpen(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return userProfileRepository.isNavbarToggled(principal.getUsername());
    }

    public boolean hideNavbar(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return userProfileRepository.hideNavbar(principal.getUsername());
    }

    public void updateLanguage(String language, Authentication authentication) {

    }

    public boolean isOpen(@NonNull String item, @NonNull String username) {
        return userProfileRepository.isOpen(item, username);
    }
}
