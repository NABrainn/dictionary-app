package lule.dictionary.dto.database.interfaces.imports.base;

import lule.dictionary.dto.database.interfaces.imports.ImportDetails;
import lule.dictionary.dto.database.interfaces.userProfile.UserProfileSettings;

public interface Import extends ImportDetails, UserProfileSettings {
    String owner();
}
