package lule.dictionary.userProfiles.data;

public interface UserProfile extends UserProfileCredentials, UserProfileSettings, CustomUserDetails {
    int wordsAddedToday();
}
