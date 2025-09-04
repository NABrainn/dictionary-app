package lule.dictionary.documents.service.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.service.dto.strategy.SubmissionStrategy;

@Builder
public record DocumentFormAttribute(@NonNull SubmissionStrategy submissionStrategy,
                                    @NonNull String titleText,
                                    @NonNull String contentText,
                                    @NonNull String importByUrlBtnText,
                                    @NonNull String insertManuallyBtnText,
                                    @NonNull String submitBtnText) {
}
