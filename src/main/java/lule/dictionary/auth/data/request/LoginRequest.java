package lule.dictionary.auth.data.request;

public record LoginRequest(
        String login,
        String password) implements AuthRequest {
    public static LoginRequest of(String login, String password) {
        return new LoginRequest(login, password);
    }
}