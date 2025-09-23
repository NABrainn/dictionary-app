package lule.dictionary.userProfiles.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.userProfiles.data.repository.UserProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInterfaceService {

    private final UserProfileRepository userProfileRepository;

    public boolean toggleNavbar(String item, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return item.equals("navbar") && userProfileRepository.toggleNavbar(principal.getUsername());
    }

    public boolean isNavbarToggled(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return userProfileRepository.isNavbarToggled(principal.getUsername());
    }

    public boolean hideNavbar(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return userProfileRepository.hideNavbar(principal.getUsername());
    }
}
