package lule.dictionary.service.imports.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record DocumentFormAttribute(@NonNull SubmissionStrategy submissionStrategy,
                                    @NonNull String titleText,
                                    @NonNull String contentText,
                                    @NonNull String importByUrlBtnText,
                                    @NonNull String insertManuallyBtnText,
                                    @NonNull String submitBtnText) {
}
