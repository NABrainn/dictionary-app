package lule.dictionary.unit.entity;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.UserProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class UserProfileTest {

    @Test
    void constructor_validParameters() {
        UserProfile user = new UserProfile("nabrain", "email@mail.pl", "password");
    }

    @Test
    void constructor_invalidUsername() {
        String[] invalidChars = {
                "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "'", "\"", "_", "=", "+", "[", "{", "]", "}", "\\", ";", ":", ",", "<", ".", ">", "/", "?", "`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
        };

        for(String ch : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> new UserProfile("nabr" + ch + "ain", "email@mail.pl", "password"));
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
            assertThrows(IllegalArgumentException.class, () -> new UserProfile("nabrain", email, "password"),
                    "Owner '" + email + "' should throw IllegalArgumentException");
        }
    }

    @Test
    void constructor_nameOver50Characters() {
        assertThrows(IllegalArgumentException.class, () -> new UserProfile("nabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrainnabrnabrainain", "e@mail", "password"));
    }

    @Test
    void constructor_passwordUnder8Characters() {
        assertThrows(IllegalArgumentException.class, () -> new UserProfile("n", "email@mail.pl", "pas"));
    }


    @Test
    void constructor_nullParameters() {
        assertThrows(NullPointerException.class, () -> new UserProfile(null, null, null));
    }
}
