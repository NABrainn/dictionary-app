package lule.dictionary.dto.database.interfaces.userProfile.base;

import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.dto.database.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.dto.database.interfaces.userProfile.UserProfileSettings;

public interface UserProfile extends UserProfileCredentials, UserProfileSettings, CustomUserDetails {
    int wordsAddedToday();
}
