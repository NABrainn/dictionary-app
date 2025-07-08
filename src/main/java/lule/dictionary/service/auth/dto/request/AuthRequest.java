package lule.dictionary.service.auth.dto.request;

import lule.dictionary.service.dto.request.ServiceRequest;

public interface AuthRequest extends ServiceRequest {
    String login();
    String password();
}
