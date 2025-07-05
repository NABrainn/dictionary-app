package lule.dictionary.service.auth.dto.authenticationResult;

import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import org.springframework.security.core.Authentication;

public record AuthenticationResult(Authentication authentication,
                                   UserProfile userProfile) {
}
