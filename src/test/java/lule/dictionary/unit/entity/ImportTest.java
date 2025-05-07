package lule.dictionary.unit.entity;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.Import;
import lule.dictionary.enumeration.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class ImportTest {

    @Test
    void constructor_validParameters() {
        Import trans = new Import("title", "wordwordwordwordwordwordwordwordwordwordwordwordwordwordwordword", "url", Language.EN, Language.NO, "nabrain");
        log.info(trans.toString());
    }

    @Test
    void constructor_titleFirstCharRegex() {
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`"
        };
        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> new Import(ch + "title", "wordwordwordwordwordwordwordwordwordwordwordwordwordwordwordword", "url", Language.EN, Language.NO, "nabrain"));
        }
    }


    @Test
    void constructor_titleOver100Characters() {
        assertThrows(IllegalArgumentException.class, () -> new Import("""
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle
                """, "wordwordwordwordwordwordwordwordwordwordwordwordwordwordwordword", "url", Language.EN, Language.NO, "nabrain"));
    }

    @Test
    void constructor_ownerOver20Characters() {
        assertThrows(IllegalArgumentException.class, () -> new Import("title", "wordwordwordwordwordwordwordwordwordwordwordwordwordwordwordword", "url", Language.EN, Language.NO, "nabrainnabrainnabrainnabrainnabrain"));
    }

    @Test
    void constructor_nullParameters() {
        assertThrows(NullPointerException.class, () -> new Import("title", null, "url", Language.EN, null, null));
    }

    @Test
    void constructor_sameLanguages() {
        assertThrows(IllegalArgumentException.class, () -> new Import("title", "wordwordwordwordwordwordwordwordwordwordwordwordwordwordwordword", "url", Language.NO, Language.NO, "nabrain"));
    }
}
