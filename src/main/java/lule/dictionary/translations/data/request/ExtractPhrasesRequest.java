package lule.dictionary.translations.data.request;

import lombok.NonNull;

public record ExtractPhrasesRequest(@NonNull String content,
                                    @NonNull String owner) {
    public static ExtractPhrasesRequest of(String content, String owner) {
        return new ExtractPhrasesRequest(content, owner);
    }
}
