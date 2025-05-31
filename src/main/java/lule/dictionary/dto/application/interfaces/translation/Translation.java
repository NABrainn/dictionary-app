package lule.dictionary.dto.application.interfaces.translation;

import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;

public interface Translation {
    TranslationDetails translationDetails();
    UserProfile userProfile();
}
