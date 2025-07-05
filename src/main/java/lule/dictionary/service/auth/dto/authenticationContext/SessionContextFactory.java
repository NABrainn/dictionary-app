package lule.dictionary.service.auth.dto.authenticationContext;

import jakarta.servlet.http.HttpServletResponse;
import lule.dictionary.service.auth.dto.authenticationResult.AuthenticationResult;
import org.springframework.stereotype.Service;

@Service
public class SessionContextFactory {
    public SessionContext of(AuthenticationResult authenticationResult,
                             HttpServletResponse response) {
        return new SessionContext(authenticationResult, response);
    }
}
