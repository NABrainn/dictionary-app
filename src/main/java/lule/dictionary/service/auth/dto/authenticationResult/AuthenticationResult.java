package lule.dictionary.service.auth.dto.authenticationResult;

import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import org.springframework.security.core.Authentication;

public record AuthenticationResult(Authentication authentication,
                                   UserProfile userProfile) {
    public static AuthenticationResult of(Authentication authentication, UserProfile user) {
        return new AuthenticationResult(authentication, user);
    }
}
