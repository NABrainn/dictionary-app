package lule.dictionary.userProfiles.data;

public interface UserProfile extends CustomUserDetails {
    String username();
    String email();
    String password();
    int wordsAddedToday();
}
