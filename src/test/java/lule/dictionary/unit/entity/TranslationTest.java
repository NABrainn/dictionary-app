package lule.dictionary.unit.entity;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.implementation.translation.DictionaryTranslation;
import lule.dictionary.dto.application.implementation.userProfile.DictionaryUserProfile;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.factory.dto.TranslationFactory;
import lule.dictionary.factory.dto.UserProfileFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class TranslationTest {

    @Test
    void constructor_validParameters() {
        TranslationFactory.createTranslation(
                TranslationFactory.createTranslationDetails(
                        "owo",
                        "uwu",
                        Familiarity.UNKNOWN
                ),
                UserProfileFactory.createUserProfile(
                        UserProfileFactory.createCredentials(
                                "nabrain",
                                "eeee@mail.com",
                                "password"
                        ),
                        UserProfileFactory.createSettings(
                                Language.EN,
                                Language.PL
                        )
                )
        );
    }

    @Test
    void constructor_invalidSourceWord() {
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "'", "\"", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`"
        };

        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> TranslationFactory.createTranslation(
                    TranslationFactory.createTranslationDetails(
                            ch + "owo" + ch,
                            "uwu",
                            Familiarity.UNKNOWN
                    ),
                    UserProfileFactory.createUserProfile(
                            UserProfileFactory.createCredentials(
                                    "nabrain",
                                    "eeee@mail.com",
                                    "password"
                            ),
                            UserProfileFactory.createSettings(
                                    Language.EN,
                                    Language.PL
                            )
                    )
            ));
        }
    }

    @Test
    void constructor_invalidTargetWord() {
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "'", "\"", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`"
        };

        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> TranslationFactory.createTranslation(
                    TranslationFactory.createTranslationDetails(
                            "owo",
                            ch + "uwu" + ch,
                            Familiarity.UNKNOWN
                    ),
                    UserProfileFactory.createUserProfile(
                            UserProfileFactory.createCredentials(
                                    "nabrain",
                                    "eeee@mail.com",
                                    "password"
                            ),
                            UserProfileFactory.createSettings(
                                    Language.EN,
                                    Language.PL
                            )
                    )
            ));
        }
    }

    @Test
    void constructor_nullParameters() {
        assertThrows(NullPointerException.class, () -> new DictionaryTranslation(null, null));
    }
}
