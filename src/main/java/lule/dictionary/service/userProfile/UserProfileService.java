package lule.dictionary.service.userProfile;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.userProfile.base.DictionaryUserProfile;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.ResourceNotFoundException;
import lule.dictionary.repository.UserProfileRepository;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    public UserProfile findByUsername(@NonNull String username) throws ServiceException {
        return userProfileRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserProfile addUserProfile(@NonNull String username, @NonNull String email, @NonNull String password) throws ServiceException {
        try {
            UserProfile userProfile = DictionaryUserProfile.builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .sourceLanguage(Language.EN)
                    .targetLanguage(Language.NO)
                    .build();
            return userProfileRepository.addUserProfile(DictionaryUserProfile.builder()
                            .username(userProfile.username())
                            .email(userProfile.email())
                            .password(userProfile.password())
                            .sourceLanguage(userProfile.sourceLanguage())
                            .targetLanguage(userProfile.targetLanguage())
                    .build()).orElseThrow(() -> new ServiceException("Failed to add user"));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to add user", e.getCause());
        }
    }

    public List<UserProfile> findAll() throws ServiceException {
        try {
            return userProfileRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException("User not found", e.getCause());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return (UserDetails) userProfileRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch user", e.getCause());
        }
    }

    public Optional<UserProfile> findByUsernameOrEmail(String username, String email) {
        return userProfileRepository.findByUsernameOrEmail(username, email);
    }
}
