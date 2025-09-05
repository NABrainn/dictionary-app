package lule.dictionary.auth.data;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public record SessionContext(AuthenticationData authenticationData,
                             HttpServletResponse response,
                             HttpSession httpSession) {
    public static SessionContext of(AuthenticationData authenticationData, HttpServletResponse response, HttpSession httpSession) {
        return new SessionContext(authenticationData, response, httpSession);
    }
}
