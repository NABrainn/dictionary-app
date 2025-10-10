package lule.dictionary.userProfiles.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.auth.data.exception.AuthServiceException;
import lule.dictionary.auth.service.SecurityContextService;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.security.data.TimeZoneOffsetContext;
import lule.dictionary.language.service.LanguageService;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.language.service.Language;
import lule.dictionary.userProfiles.data.repository.UserProfileRepository;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import lule.dictionary.date.service.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService implements UserDetailsService {

    private final BCryptPasswordEncoder encoder;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public void addUserProfile(@NonNull SignupRequest signupRequest) {
        UserProfile userProfile = UserProfile.builder()
                .username(signupRequest.login())
                .email(signupRequest.email())
                .password(encoder.encode(signupRequest.password()))
                .sourceLanguage(Language.EN)
                .targetLanguage(Language.NO)
                .userInterfaceLanguage(Language.EN)
                .wordsAddedToday(0)
                .dailyStreak(0)
                .offset(TimeZoneOffsetContext.get())
                .isNavbarOpen(false)
                .build();
        userProfileRepository.addUserProfile(userProfile).orElseThrow(() -> new RuntimeException("Failed to add new user"));
    }

    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UserNotFoundException {
        return userProfileRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public Result<?> loadByUsernameOrEmail(String username, String email, String errorMessage) {
        var errorMap = Map.of("userExists", errorMessage);
        var throwable = new AuthServiceException(errorMap);
        return userProfileRepository.findByUsernameOrEmail(username, email)
                .map(_ -> (Result<?>) Err.of(throwable))
                .orElse(Ok.empty());
    }

    public void updateTimezoneOffset(String owner, String offset) {
        if(offset != null) {
            userProfileRepository.updateTimezoneOffset(owner, DateUtil.stringToZoneOffset(offset).getId());
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void resetStreaksIfMidnight() {
        userProfileRepository.resetStreaksIfMidnight();
    }
}
