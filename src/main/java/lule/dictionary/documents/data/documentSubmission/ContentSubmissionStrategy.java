package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;

public record ContentSubmissionStrategy(@NonNull String title,
                                        @NonNull String content,
                                        @NonNull String contentPlaceholderText) implements SubmissionStrategy {
    public static ContentSubmissionStrategy of(String title, String content, String contentPlaceholderText) {
        return new ContentSubmissionStrategy(title, content, contentPlaceholderText);
    }
}