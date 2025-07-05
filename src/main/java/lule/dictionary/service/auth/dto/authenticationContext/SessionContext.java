package lule.dictionary.service.auth.dto.authenticationContext;

import jakarta.servlet.http.HttpServletResponse;
import lule.dictionary.service.auth.dto.authenticationResult.AuthenticationResult;

public record SessionContext(AuthenticationResult authenticationResult,
                             HttpServletResponse response) {
}
