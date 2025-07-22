package lule.dictionary.service.auth.dto.authenticationContext;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lule.dictionary.service.auth.dto.authenticationResult.AuthenticationData;

public record SessionContext(AuthenticationData authenticationData,
                             HttpServletResponse response,
                             HttpSession httpSession) {
    public static SessionContext of(AuthenticationData authenticationData, HttpServletResponse response, HttpSession httpSession) {
        return new SessionContext(authenticationData, response, httpSession);
    }
}
