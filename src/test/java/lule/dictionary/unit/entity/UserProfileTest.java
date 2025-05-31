package lule.dictionary.unit.entity;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.language.bm.Lang;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.implementation.userProfile.DictionaryUserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.enumeration.Language;
import lule.dictionary.factory.dto.UserProfileFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class UserProfileTest {

    @Test
    void constructor_validParameters() {
        UserProfile user = new DictionaryUserProfile(
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
    }

    @Test
    void constructor_invalidUsername() {
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "'", "\"", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
        };

        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> new DictionaryUserProfile(
                    UserProfileFactory.createCredentials(
                            "nabr" + ch + "ain",
                            "email@mail.pl",
                            "password"
                    ),
                    UserProfileFactory.createSettings(
                            Language.EN,
                            Language.PL
                    )
            ));
        }
    }

    @Test
    void constructor_invalidEmail() {
        String[] invalidEmails = {
                "nabrain",
                "user@",
                "user@domain",
                "user@.com",
                "@domain.com",
                "user@domain.",
                "user#name@domain.com",
        };
        for (String email : invalidEmails) {
            assertThrows(IllegalArgumentException.class, () -> new DictionaryUserProfile(
                            UserProfileFactory.createCredentials(
                                    "nabrain",
                                    email,
                                    "password"
                            ),
                            UserProfileFactory.createSettings(
                                    Language.EN,
                                    Language.PL
                            )
                    ),
                    "Owner '" + email + "' should throw IllegalArgumentException");
        }
    }

    @Test
    void constructor_nameOver50Characters() {
        String badUsername = "nabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrnabrainain";
        assertThrows(IllegalArgumentException.class, () -> new DictionaryUserProfile(
                UserProfileFactory.createCredentials(
                        badUsername,
                        "e@mail.com",
                        "password"
                ),
                UserProfileFactory.createSettings(
                        Language.EN,
                        Language.PL
                )
        ));
    }

    @Test
    void constructor_passwordUnder8Characters() {
        assertThrows(IllegalArgumentException.class, () -> new DictionaryUserProfile(
                UserProfileFactory.createCredentials(
                        "nabrain",
                        "email@email.pl",
                        "pas"
                ),
                UserProfileFactory.createSettings(
                        Language.EN,
                        Language.PL
                )
        ));
    }


    @Test
    void constructor_nullParameters() {
        assertThrows(NullPointerException.class, () -> new DictionaryUserProfile(
                UserProfileFactory.createCredentials(
                        null,
                        null,
                        null
                ),
                UserProfileFactory.createSettings(
                        Language.EN,
                        Language.PL
                )
        ));
    }
}
