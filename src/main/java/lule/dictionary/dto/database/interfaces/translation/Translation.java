package lule.dictionary.dto.database.interfaces.translation;

import lule.dictionary.dto.database.interfaces.userProfile.UserProfileSettings;

public interface Translation extends TranslationDetails, UserProfileSettings {
    String owner();
}
