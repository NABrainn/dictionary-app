package lule.dictionary.unit.entity;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.implementation.imports.DictionaryImport;
import lule.dictionary.dto.application.implementation.userProfile.DictionaryUserProfile;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.enumeration.Language;
import lule.dictionary.factory.dto.ImportFactory;
import lule.dictionary.factory.dto.UserProfileFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class ImportTest {

    @Test
    void constructor_validParameters() {
        UserProfile userProfile = new DictionaryUserProfile(
                UserProfileFactory.createCredentials(
                        "nabrain",
                        "email@mail.pl",
                        "password"
                ),
                UserProfileFactory.createSettings(
                        Language.EN,
                        Language.PL
                )
        );
        Import importt = ImportFactory.createImport(
                ImportFactory.createImportDetails(
                        "title",
                        "wordwordwordwordwordwordwordwordwordwordwordwordwordwordwordword",
                        "url"
                ),
                userProfile.userProfileSettings(),
                userProfile.userProfileCredentials().username()
        );
    }

    @Test
    void constructor_invalidFirstChar() {
        UserProfile userProfile = new DictionaryUserProfile(
                UserProfileFactory.createCredentials(
                        "nabrain",
                        "email@mail.pl",
                        "password"
                ),
                UserProfileFactory.createSettings(
                        Language.EN,
                        Language.PL
                )
        );
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`"
        };
        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> ImportFactory.createImport(
                    ImportFactory.createImportDetails(
                            ch + "title",
                            "wordwordwordwordwordwordwordwordwordwordwordwordwordwordwordword",
                            "url"
                    ),
                    userProfile.userProfileSettings(),
                    userProfile.userProfileCredentials().username()
            ));
        }
    }


    @Test
    void constructor_titleOver100Characters() {
        UserProfile userProfile = new DictionaryUserProfile(
                UserProfileFactory.createCredentials(
                        "nabrain",
                        "email@mail.pl",
                        "password"
                ),
                UserProfileFactory.createSettings(
                        Language.EN,
                        Language.PL
                )
        );
        String badTitle = """
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                """;
        assertThrows(IllegalArgumentException.class, () -> ImportFactory.createImport(
                ImportFactory.createImportDetails(
                        badTitle,
                        "wordwordwordwordwordwordwordwordwordwordwordwordwordwordwordword",
                        "url"
                ),
                userProfile.userProfileSettings(),
                userProfile.userProfileCredentials().username()
        ));
    }


    @Test
    void constructor_nullParameters() {
        UserProfile userProfile = new DictionaryUserProfile(
                UserProfileFactory.createCredentials(
                        "nabrain",
                        "email@mail.pl",
                        "password"
                ),
                UserProfileFactory.createSettings(
                        Language.EN,
                        Language.PL
                )
        );
        assertThrows(NullPointerException.class, () -> ImportFactory.createImport(
                ImportFactory.createImportDetails(
                        null,
                        null,
                        null
                ),
                null,
                null
        ));
    }
}
