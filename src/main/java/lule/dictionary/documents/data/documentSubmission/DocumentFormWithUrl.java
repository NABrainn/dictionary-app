package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;

public record DocumentFormWithUrl(@NonNull String title,
                                  @NonNull String url,
                                  @NonNull String urlPlaceholderText) implements DocumentFormType {
    public static DocumentFormWithUrl of(String title, String url, String urlPlaceholderText) {
        return new DocumentFormWithUrl(title, url, urlPlaceholderText);
    }
}