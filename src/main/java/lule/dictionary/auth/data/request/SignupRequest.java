package lule.dictionary.auth.data.request;

public record SignupRequest(
        String login,
        String email,
        String password) implements AuthRequest {
    public static SignupRequest of(String login, String email, String password) {
        return new SignupRequest(login, email, password);
    }
}