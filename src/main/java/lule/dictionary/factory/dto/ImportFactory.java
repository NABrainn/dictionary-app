package lule.dictionary.factory.dto;

import lule.dictionary.dto.application.implementation.imports.DictionaryImport;
import lule.dictionary.dto.application.implementation.imports.DictionaryImportDetails;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

public class ImportFactory {
    public static Import createImport(
            ImportDetails importDetails,
            UserProfileSettings userProfileSettings,
            String owner) {
        return new DictionaryImport(importDetails, userProfileSettings, owner);
    }

    public static ImportDetails createImportDetails(
            String title,
            String content,
            String url) {
        return new DictionaryImportDetails(title, content, url);
    }
}
