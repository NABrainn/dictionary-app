package lule.dictionary.dto.application.interfaces.imports.base;

import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public interface Import {
    ImportDetails importDetails();
    UserProfileSettings userProfileSettings();
    String owner();
}
