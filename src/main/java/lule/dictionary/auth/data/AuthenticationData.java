package lule.dictionary.auth.data;

import lule.dictionary.userProfiles.data.UserProfile;
import org.springframework.security.core.Authentication;

public record AuthenticationData(Authentication authentication,
                                 UserProfile userProfile) {
    public static AuthenticationData of(Authentication authentication, UserProfile user) {
        return new AuthenticationData(authentication, user);
    }
}
