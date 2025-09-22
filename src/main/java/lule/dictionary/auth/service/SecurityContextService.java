package lule.dictionary.auth.service;

import lule.dictionary.userProfiles.data.UserProfile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {
    public void authenticateAndSetContext(UserProfile user, AuthenticationManager authenticationManager) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setJwtContext(UsernamePasswordAuthenticationToken token, WebAuthenticationDetails requestToken) {
        token.setDetails(requestToken);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void setContext(UsernamePasswordAuthenticationToken token) {
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void clearContext() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }
}
