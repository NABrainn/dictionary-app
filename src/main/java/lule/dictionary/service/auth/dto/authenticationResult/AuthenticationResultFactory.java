package lule.dictionary.service.auth.dto.authenticationResult;

import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationResultFactory {

    public AuthenticationResult of(Authentication authentication, UserProfile userProfile) {
        return new AuthenticationResult(authentication, userProfile);
    }
}
