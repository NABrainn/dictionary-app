package lule.dictionary.service.userProfile;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.implementation.userProfile.base.UserProfileImp;
import lule.dictionary.entity.application.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.service.auth.dto.request.imp.SignupRequest;
import lule.dictionary.service.language.Language;
import lule.dictionary.repository.UserProfileRepository;
import lule.dictionary.service.userProfile.exception.UserNotFoundException;
import lule.dictionary.util.DateUtil;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserProfile getUserProfile(@NonNull String username) {
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
                .wordsAddedToday(0)
                .offset("")
                .build();
        userProfileRepository.addUserProfile(userProfile).orElseThrow(() -> new RuntimeException("Failed to add new user"));
    }

    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userProfileRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Optional<UserProfile> findByUsernameOrEmail(String username, String email) throws UserNotFoundException {
        return userProfileRepository.findByUsernameOrEmail(username, email);
    }

    public void updateTimezoneOffset(String owner, String offset) {
        if(offset != null) {
            userProfileRepository.updateTimezoneOffset(
                    owner,
                    DateUtil.stringToZoneOffset(offset).getId()
            );
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
        return bCryptPasswordEncoder.encode(password);
    }
}
