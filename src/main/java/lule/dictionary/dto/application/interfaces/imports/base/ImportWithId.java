package lule.dictionary.dto.application.interfaces.imports.base;

import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public interface ImportWithId {
    ImportDetails importDetails();
    UserProfileSettings userProfileSettings();
    String owner();
    int id();
}
