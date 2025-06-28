package lule.dictionary.service.userProfile;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.userProfile.base.DictionaryUserProfile;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.enumeration.Language;
import lule.dictionary.repository.UserProfileRepository;
import lule.dictionary.service.userProfile.exception.UserExistsException;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    public UserProfile findByUsername(@NonNull String username) {
        return userProfileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public UserProfile addUserProfile(@NonNull String username,
                                      @NonNull String email,
                                      @NonNull String password) {
        UserProfile userProfile = DictionaryUserProfile.builder()
                .username(username)
                .email(email)
                .password(password)
                .sourceLanguage(Language.EN)
                .targetLanguage(Language.NO)
                .build();
        return userProfileRepository.addUserProfile(
                DictionaryUserProfile.builder()
                .username(userProfile.username())
                .email(userProfile.email())
                .password(userProfile.password())
                .sourceLanguage(userProfile.sourceLanguage())
                .targetLanguage(userProfile.targetLanguage())
                .build()
        ).orElseThrow(() -> new UserExistsException("User already exists"));
    }

    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userProfileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Optional<UserProfile> findByUsernameOrEmail(String username, String email) {
        return userProfileRepository.findByUsernameOrEmail(username, email);
    }
}
