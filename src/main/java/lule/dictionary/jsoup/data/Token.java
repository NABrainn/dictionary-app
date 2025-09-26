package lule.dictionary.jsoup.data;

import lombok.NonNull;

public record Token(@NonNull String content,
                    int newlineCount) {
    public static Token of(String content, int newlineCount) {
        return new Token(content, newlineCount);
    }

    public Token withContent(@NonNull String content) {
        return new Token(content, this.newlineCount);
    }
}
