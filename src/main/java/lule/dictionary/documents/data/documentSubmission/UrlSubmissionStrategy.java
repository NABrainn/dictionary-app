package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;

import java.util.Map;
import java.util.Set;

public record UrlSubmissionStrategy(@NonNull String title,
                                    @NonNull String url,
                                    @NonNull String spaceForUrlText) implements SubmissionStrategy, Validated {
    public static UrlSubmissionStrategy of(String spaceForUrlText, String title, String url) {
        return new UrlSubmissionStrategy(spaceForUrlText, title, url);
    }

    @Override
    public Set<Map<String, String>> validate(Language language) {
        return Set.of();
    }
}
