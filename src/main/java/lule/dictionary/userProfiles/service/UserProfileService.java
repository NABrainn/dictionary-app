package lule.dictionary.userProfiles.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.language.service.Language;
import lule.dictionary.userProfiles.data.repository.UserProfileRepository;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import lule.dictionary.date.service.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder encoder;

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
                .build();
        userProfileRepository.addUserProfile(userProfile).orElseThrow(() -> new RuntimeException("Failed to add new user"));
    }

    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UserNotFoundException {
        return userProfileRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public Optional<UserProfile> findByUsernameOrEmail(String username, String email) {
        return userProfileRepository.findByUsernameOrEmail(username, email);
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

    public int getDailyStreak(String owner) {
        return userProfileRepository.getDailyStreak(owner).orElseThrow(() -> new RuntimeException("Failed to fetch daily streak"));
    }

    //TODO merge below methods into one
    public void updateTargetLanguage(String languageString, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Stream.of(languageString)
                .map(lang -> {
                    try {
                        return Language.valueOf(lang);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresentOrElse(
                        value -> {
                            userProfileRepository.updateTargetLanguage(principal.getUsername(), value.name());
                            UserProfile userDetails = (UserProfile) loadUserByUsername(principal.getUsername());
                            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
                        },
                        () -> { throw new RuntimeException("Illegal value for language provided"); }
                );

    }

    public void updateSourceLanguage(String languageString, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Stream.of(languageString)
                .map(lang -> {
                    try {
                        return Language.valueOf(lang);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresentOrElse(
                        value -> {
                            userProfileRepository.updateSourceLanguage(principal.getUsername(), value.name());
                            UserProfile userDetails = (UserProfile) loadUserByUsername(principal.getUsername());
                            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
                        },
                        () -> { throw new RuntimeException("Illegal value for language provided"); }
                );

    }

    public void updateUILanguage(String languageString, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Stream.of(languageString)
                .map(lang -> {
                    try {
                        return Language.valueOf(lang);
                    }
                    catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresentOrElse(
                        value -> {
                            userProfileRepository.updateUILanguage(principal.getUsername(), value.name());
                            UserProfile userDetails = (UserProfile) loadUserByUsername(principal.getUsername());
                            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
                        },
                        () -> { throw new RuntimeException("Illegal value for language provided"); }
                );
    }
}
