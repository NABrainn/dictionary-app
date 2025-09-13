package lule.dictionary.documents.data.request;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.security.core.Authentication;

@Builder
public record CreateDocumentRequest(@NonNull String submissionStrategy,
                                    @NonNull Authentication authentication,
                                    @NonNull String title,
                                    @NonNull String content,
                                    @NonNull String url) {
}