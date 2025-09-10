package lule.dictionary.documents.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.data.documentSubmission.SubmissionStrategy;

@Builder
public record DocumentFormAttribute(@NonNull SubmissionStrategy submissionStrategy,
                                    @NonNull String titleText,
                                    @NonNull String contentText,
                                    @NonNull String importByUrlBtnText,
                                    @NonNull String insertManuallyBtnText,
                                    @NonNull String submitBtnText) {
}
