package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;

public record DocumentFormWithContent(@NonNull String title,
                                      @NonNull String content,
                                      @NonNull String contentPlaceholderText) implements DocumentFormType {
    public static DocumentFormWithContent of(String title, String content, String contentPlaceholderText) {
        return new DocumentFormWithContent(title, content, contentPlaceholderText);
    }
}