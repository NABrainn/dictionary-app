package lule.dictionary.dto.application.implementation.imports;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;

@Slf4j
public record DictionaryImport(
        @NonNull
        ImportDetails importDetails,
        @NonNull
        UserProfile userProfile) implements Import {
}
