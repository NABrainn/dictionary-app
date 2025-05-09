package lule.dictionary.unit.entity;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class TranslationTest {

    @Test
    void constructor_validParameters() {
        Translation trans = new Translation("słowo", "word", Language.PL, Language.EN, "nabrain", Familiarity.UNKNOWN);
    }

    @Test
    void constructor_invalidSourceWord() {
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "'", "\"", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`"
        };

        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> new Translation("s" + ch + "owo./,;'[]-=<>?:{}_+-=", "word", Language.PL, Language.EN, "nabrain", Familiarity.RECOGNIZED));
        }
    }

    @Test
    void constructor_invalidTargetWord() {
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "'", "\"", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`"
        };

        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> new Translation("sowo./,;'[]-=<>?:{}_+-=", "wo" + ch + "rd", Language.PL, Language.EN, "nabrain", Familiarity.RECOGNIZED));
        }
    }

    @Test
    void constructor_invalidTranslationOwner() {
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "'", "\"", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`"
        };

        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> new Translation("sowo./,;'[]-=<>?:{}_+-=", "word", Language.PL, Language.EN, "nabr" + ch + "ain", Familiarity.RECOGNIZED));
        }
    }

    @Test
    void constructor_nameOver50Characters() {
        assertThrows(IllegalArgumentException.class, () -> new Translation("słowosłowosłowosłowosłowosłowosłowosłowosłowosłowosłowosłowosłowosłowosłowosłowosłowo", "word", Language.PL, Language.EN, "nabrain", Familiarity.IGNORED));
    }

    @Test
    void constructor_ownerOver20Characters() {
        assertThrows(IllegalArgumentException.class, () -> new Translation("słowo", "word", Language.PL, Language.EN, "nabrainnabrainnabrainnabrainnabrainnabrainnabrain", Familiarity.KNOWN));
    }

    @Test
    void constructor_nullParameters() {
        assertThrows(NullPointerException.class, () -> new Translation(null, "word", Language.PL, null, "nabrain", Familiarity.KNOWN));
    }

    @Test
    void constructor_sameLanguages() {
        assertThrows(IllegalArgumentException.class, () -> new Translation("słowo", "word", Language.PL, Language.PL, "nabrain", Familiarity.KNOWN));
    }
}
