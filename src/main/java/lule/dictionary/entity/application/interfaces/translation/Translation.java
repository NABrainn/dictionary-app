package lule.dictionary.entity.application.interfaces.translation;

import lule.dictionary.entity.application.interfaces.userProfile.UserProfileSettings;

public interface Translation extends TranslationDetails, UserProfileSettings {
    String owner();
}
