package lule.dictionary.service.auth.dto.authenticationContext;

import jakarta.servlet.http.HttpServletResponse;
import lule.dictionary.service.auth.dto.authenticationResult.AuthenticationData;

public record SessionContext(AuthenticationData authenticationData,
                             HttpServletResponse response) {
    public static SessionContext of(AuthenticationData authenticationData, HttpServletResponse response) {
        return new SessionContext(authenticationData, response);
    }
}
