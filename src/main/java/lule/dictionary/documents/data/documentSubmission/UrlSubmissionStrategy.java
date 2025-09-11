package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;

public record UrlSubmissionStrategy(@NonNull String title,
                                    @NonNull String url,
                                    @NonNull String spaceForUrlText) implements SubmissionStrategy {
    public static UrlSubmissionStrategy of(String spaceForUrlText, String title, String url) {
        return new UrlSubmissionStrategy(spaceForUrlText, title, url);
    }
}
