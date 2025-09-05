package lule.dictionary.userProfiles.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import lule.dictionary.userProfiles.data.UserProfileImp;
import lule.dictionary.userProfiles.data.CustomUserDetails;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.language.service.Language;
import lule.dictionary.userProfiles.data.repository.UserProfileRepository;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import lule.dictionary.shared.DateUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder encoder;

    public UserProfile getUserProfile(@NonNull String username) throws UserNotFoundException {
        return userProfileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with given username does not exist"));
    }

    @Transactional
    public void addUserProfile(@NonNull SignupRequest signupRequest) {
        UserProfile userProfile = UserProfileImp.builder()
                .username(signupRequest.login())
                .email(signupRequest.email())
                .password(encode(signupRequest.password()))
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userProfileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Optional<UserProfile> findByUsernameOrEmail(String username, String email) throws UserNotFoundException {
        return userProfileRepository.findByUsernameOrEmail(username, email);
    }

    public void updateTimezoneOffset(String owner, String offset) {
        if(offset != null) {
            userProfileRepository.updateTimezoneOffset(owner, DateUtil.stringToZoneOffset(offset).getId());
        }
    }
    public void resetStreaksIfMidnight() {
        userProfileRepository.resetStreaksIfMidnight();
    }

    public int getDailyStreak(String owner) {
        return userProfileRepository.getDailyStreak(owner).orElseThrow(() -> new RuntimeException("Failed to fetch daily streak"));
    }

    public void updateTargetLanguage(String owner, Language newTargetLanguage) {
        userProfileRepository.updateTargetLanguage(owner, newTargetLanguage.name());
        CustomUserDetails userDetails = (CustomUserDetails) loadUserByUsername(owner);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        ));
    }

    private String encode(String password) {
        return encoder.encode(password);
    }

    public void updateSourceLanguage(String username, Language language) {
        userProfileRepository.updateSourceLanguage(username, language.name());
        CustomUserDetails userDetails = (CustomUserDetails) loadUserByUsername(username);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        ));
    }

    public void updateUILanguage(String username, Language uiLanguage) {
        userProfileRepository.updateUILanguage(username, uiLanguage.name());
        CustomUserDetails userDetails = (CustomUserDetails) loadUserByUsername(username);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        ));
    }
}
