package lule.dictionary.dto.application.interfaces.translation;

import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public interface Translation {
    TranslationDetails translationDetails();
    UserProfileSettings userProfileSettings();
    String owner();
}
