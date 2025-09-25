package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;

public record UrlSubmissionStrategy(@NonNull String title,
                                    @NonNull String url,
                                    @NonNull String urlPlaceholderText) implements SubmissionStrategy {
    public static UrlSubmissionStrategy of(String title, String url, String urlPlaceholderText) {
        return new UrlSubmissionStrategy(title, url, urlPlaceholderText);
    }
}