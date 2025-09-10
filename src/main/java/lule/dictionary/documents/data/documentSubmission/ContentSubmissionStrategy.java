package lule.dictionary.documents.data.documentSubmission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record ContentSubmissionStrategy(@NonNull @NotBlank @Size(min = 10, max = 200) String title,
                                        @NonNull @NotBlank @Size(max = 1000000) String content,
                                        @NonNull String spaceForContentText) implements SubmissionStrategy {
    public static ContentSubmissionStrategy of(String spaceForContentText, String title, String content) {
        return new ContentSubmissionStrategy(spaceForContentText, title, content);
    }
}
