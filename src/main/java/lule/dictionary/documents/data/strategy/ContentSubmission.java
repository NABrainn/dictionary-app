package lule.dictionary.documents.data.strategy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record ContentSubmission(@NonNull
                                @NotBlank @Size(min = 10, max = 200) String title,
                                @NonNull
                                @NotBlank @Size(max = 1000000) String content,
                                @NonNull String spaceForContentText) implements SubmissionStrategy {
    public static ContentSubmission of(String spaceForContentText, String title, String content) {
        return new ContentSubmission(spaceForContentText, title, content);
    }
}
