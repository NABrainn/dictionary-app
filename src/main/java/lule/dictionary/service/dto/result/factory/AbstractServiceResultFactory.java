package lule.dictionary.service.dto.result.factory;

import lule.dictionary.service.auth.dto.result.dto.LoginResult;
import lule.dictionary.service.auth.dto.result.dto.LogoutResult;
import lule.dictionary.service.auth.dto.result.dto.SignupResult;

import java.util.Map;

public interface AbstractServiceResultFactory {
    LoginResult createLoginResult(boolean isError, Map<String, String> messages);
    SignupResult createSignupResult(boolean isError, Map<String, String> messages);
    LogoutResult createLogoutResult(boolean isError, Map<String, String> messages);
}
