package lule.dictionary.dto.application.implementation.imports;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

@Slf4j
public record DictionaryImport(
        @NonNull
        ImportDetails importDetails,
        @NonNull
        UserProfileSettings userProfileSettings,
        @NonNull
        String owner) implements Import {

}
