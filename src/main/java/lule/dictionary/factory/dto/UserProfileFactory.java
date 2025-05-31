package lule.dictionary.factory.dto;

import lule.dictionary.dto.application.implementation.userProfile.DictionaryUserProfile;
import lule.dictionary.dto.application.implementation.userProfile.DictionaryUserProfileCredentials;
import lule.dictionary.dto.application.implementation.userProfile.DictionaryUserProfileSettings;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.enumeration.Language;

public class UserProfileFactory {
    public static UserProfile createUserProfile(UserProfileCredentials userProfileCredentials, UserProfileSettings userProfileSettings) {
        return new DictionaryUserProfile(userProfileCredentials, userProfileSettings);
    }
    public static UserProfileSettings createSettings(Language sourceLanguage, Language targetLanguage) {
        return new DictionaryUserProfileSettings(sourceLanguage, targetLanguage);
    }

    public static UserProfileCredentials createCredentials(String username, String email, String password) {
        return new DictionaryUserProfileCredentials(username, email, password);
    }
}
