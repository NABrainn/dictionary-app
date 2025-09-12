package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;

import java.util.Map;
import java.util.Set;

public record ContentSubmissionStrategy(@NonNull String title,
                                        @NonNull String content,
                                        @NonNull String spaceForContentText) implements SubmissionStrategy, Validated {
    public static ContentSubmissionStrategy of(String spaceForContentText, String title, String content) {
        return new ContentSubmissionStrategy(spaceForContentText, title, content);
    }

    @Override
    public Set<Map<String, String>> validate(Language language) {
        return Set.of();
    }
}
