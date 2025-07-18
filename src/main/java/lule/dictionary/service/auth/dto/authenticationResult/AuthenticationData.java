package lule.dictionary.service.auth.dto.authenticationResult;

import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import org.springframework.security.core.Authentication;

public record AuthenticationData(Authentication authentication,
                                 UserProfile userProfile) {
    public static AuthenticationData of(Authentication authentication, UserProfile user) {
        return new AuthenticationData(authentication, user);
    }
}
