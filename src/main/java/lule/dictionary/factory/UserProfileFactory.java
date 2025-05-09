package lule.dictionary.factory;

import lule.dictionary.entity.UserProfile;

public class UserProfileFactory {
    public static UserProfile create(String username, String email, String password) {
        return new UserProfile(username, email, password);
    }
}
