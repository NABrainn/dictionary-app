package lule.dictionary.dto.application.interfaces.imports;

import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;

public interface Import {
    ImportDetails importDetails();
    UserProfile userProfile();
}
