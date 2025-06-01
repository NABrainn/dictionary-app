package lule.dictionary.dto.application.interfaces.imports;

import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public interface Import {
    ImportDetails importDetails();
    UserProfileSettings userProfileSettings();
    String owner();
}
