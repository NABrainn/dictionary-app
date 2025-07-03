package lule.dictionary.service.userProfile;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.userProfile.base.DictionaryUserProfile;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.language.Language;
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
                                      @NonNull String password,
                                      @NonNull String timeZone) {
        UserProfile userProfile = DictionaryUserProfile.builder()
                .username(username)
                .email(email)
                .password(password)
                .sourceLanguage(Language.EN)
                .targetLanguage(Language.NO)
                .wordsAddedToday(0)
                .offset(timeZone)
                .build();
        return userProfileRepository.addUserProfile(userProfile).orElseThrow(() -> new UserExistsException("Failed to signup"));
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

    public void updateTimezoneOffset(String owner, String offset) {
        userProfileRepository.updateTimezoneOffset(owner, offset);
    }

    public void resetStreaksIfMidnight() {
        userProfileRepository.resetStreaksIfMidnight();
    }

    public int getDailyStreak(String owner) {
        return userProfileRepository.getDailyStreak(owner).orElseThrow(() -> new RuntimeException("Failed to fetch daily streak"));
    }
}
