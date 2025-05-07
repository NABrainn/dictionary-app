package lule.dictionary.entity.factory;

import lule.dictionary.entity.Translation;
import lule.dictionary.entity.UserProfile;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

public class UserProfileFactory {
    public static UserProfile create(String username, String email, String password) {
        return new UserProfile(username, email, password);
    }
}
