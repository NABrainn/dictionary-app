package lule.dictionary.service.auth.dto.request;

import lule.dictionary.service.auth.dto.request.imp.LoginRequest;
import lule.dictionary.service.auth.dto.request.imp.SignupRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthRequestFactory {
    public LoginRequest ofLoginRequest(String login, String password) {
        return new LoginRequest(login, password);
    }

    public SignupRequest ofSignupRequest(String login, String email, String password) {
        return new SignupRequest(login, email, password);
    }

}
