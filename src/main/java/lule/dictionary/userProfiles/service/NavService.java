package lule.dictionary.userProfiles.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.auth.service.SecurityContextService;
import lule.dictionary.userProfiles.data.*;
import lule.dictionary.userProfiles.data.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class NavService {

    private final UserProfileService userProfileService;
    private final UserProfileRepository userProfileRepository;
    private final SecurityContextService securityContextService;

    public boolean toggle(String item, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return item.equals("progressionPanel") && userProfileRepository.toggleNavbar(principal.getUsername());
    }

    public boolean isOpen(@NonNull String item, @NonNull String username) {
        return userProfileRepository.isOpen(item, username);
    }

    public void updateLanguage(ChangeLanguageRequest request, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        switch (request) {
            case ChangeSourceLanguageRequest changeSourceLanguageRequest -> {
                userProfileRepository.updateSourceLanguage(principal.getUsername(), changeSourceLanguageRequest.language().name());
                UserProfile user = (UserProfile) userProfileService.loadUserByUsername(principal.getUsername());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                securityContextService.setContext(token);
            }
            case ChangeTargetLanguageRequest changeTargetLanguageRequest -> {
                userProfileRepository.updateTargetLanguage(principal.getUsername(), changeTargetLanguageRequest.language().name());
                UserProfile user = (UserProfile) userProfileService.loadUserByUsername(principal.getUsername());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                securityContextService.setContext(token);
            }
            case ChangeUiLanguageRequest changeUiLanguageRequest -> {
                userProfileRepository.updateUILanguage(principal.getUsername(), changeUiLanguageRequest.language().name());
                UserProfile user = (UserProfile) userProfileService.loadUserByUsername(principal.getUsername());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                securityContextService.setContext(token);
            }
        }
    }
}
