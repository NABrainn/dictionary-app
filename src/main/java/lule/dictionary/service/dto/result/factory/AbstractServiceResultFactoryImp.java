package lule.dictionary.service.dto.result.factory;

import lule.dictionary.service.auth.dto.result.dto.LoginResult;
import lule.dictionary.service.auth.dto.result.dto.LogoutResult;
import lule.dictionary.service.auth.dto.result.dto.SignupResult;

import java.util.Map;

public class AbstractServiceResultFactoryImp implements AbstractServiceResultFactory{
    @Override
    public LoginResult createLoginResult(boolean isError, Map<String, String> messages) {
        return new LoginResult(null, isError, messages);
    }

    @Override
    public SignupResult createSignupResult(boolean isError, Map<String, String> messages) {
        return new SignupResult(null, isError, messages);
    }

    @Override
    public LogoutResult createLogoutResult(boolean isError, Map<String, String> messages) {
        return new LogoutResult(null, isError, messages);
    }
}
