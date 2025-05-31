package lule.dictionary.dto.application.implementation.userProfile;


import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

@Builder
public record DictionaryUserProfile(
        @NonNull
        UserProfileCredentials userProfileCredentials,
        UserProfileSettings userProfileSettings) implements UserProfile {
}
