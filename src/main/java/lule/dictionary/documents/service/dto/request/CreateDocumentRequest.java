package lule.dictionary.documents.service.dto.request;

import lombok.NonNull;
import lule.dictionary.userProfiles.data.CustomUserDetails;
import lule.dictionary.documents.service.dto.strategy.SubmissionStrategy;

public record CreateDocumentRequest(@NonNull
                                    SubmissionStrategy submissionStrategy,

                                    @NonNull
                                    CustomUserDetails userDetails) {
    public static CreateDocumentRequest of(SubmissionStrategy submissionStrategy, CustomUserDetails customUserDetails) {
        return new CreateDocumentRequest(submissionStrategy, customUserDetails);
    }
}