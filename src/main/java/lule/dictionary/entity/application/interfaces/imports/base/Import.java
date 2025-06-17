package lule.dictionary.entity.application.interfaces.imports.base;

import lule.dictionary.entity.application.interfaces.imports.ImportDetails;
import lule.dictionary.entity.application.interfaces.userProfile.UserProfileSettings;

public interface Import extends ImportDetails, UserProfileSettings {
    String owner();
}
