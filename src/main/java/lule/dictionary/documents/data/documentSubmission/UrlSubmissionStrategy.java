package lule.dictionary.documents.data.documentSubmission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.hibernate.validator.constraints.URL;

public record UrlSubmissionStrategy(@NonNull
                            @NotBlank @Size(min = 10, max = 200) String title,
                                    @NonNull
                            @NotNull @Size(max = 200) @URL(protocol = "https") String url,
                                    @NonNull String spaceForUrlText) implements SubmissionStrategy {
    public static UrlSubmissionStrategy of(String spaceForUrlText, String title, String url) {
        return new UrlSubmissionStrategy(spaceForUrlText, title, url);
    }
}
