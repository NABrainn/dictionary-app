package lule.dictionary.entity.application.interfaces.userProfile;

import lule.dictionary.entity.application.implementation.userProfile.base.UserProfileImp;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class UserProfileFactory {

    public UserProfile withNewPassword(UserProfile userProfile, String password) {
        return new UserProfileImp(userProfile.username(), userProfile.email(), password, userProfile.sourceLanguage(), userProfile.targetLanguage(), userProfile.wordsAddedToday(), userProfile.offset());
    }
}
