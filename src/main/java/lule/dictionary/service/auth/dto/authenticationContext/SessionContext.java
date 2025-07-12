package lule.dictionary.service.auth.dto.authenticationContext;

import jakarta.servlet.http.HttpServletResponse;
import lule.dictionary.service.auth.dto.authenticationResult.AuthenticationResult;

public record SessionContext(AuthenticationResult authenticationResult,
                             HttpServletResponse response) {
    public static SessionContext of(AuthenticationResult authResult, HttpServletResponse response) {
        return new SessionContext(authResult, response);
    }
}
