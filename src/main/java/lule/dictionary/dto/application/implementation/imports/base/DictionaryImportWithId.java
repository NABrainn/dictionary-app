package lule.dictionary.dto.application.implementation.imports.base;

import lombok.NonNull;
import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.imports.base.ImportWithId;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public record DictionaryImportWithId(
        @NonNull
        ImportDetails importDetails,
        @NonNull
        UserProfileSettings userProfileSettings,
        @NonNull
        String owner,
        int id) implements ImportWithId {
}
