package lule.dictionary.dto.application.interfaces.translation;

import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public interface Translation extends TranslationDetails, UserProfileSettings {
    String owner();
}
