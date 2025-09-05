package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.documents.data.strategy.SubmissionStrategy;

public record CreateDocumentRequest(@NonNull
                                    SubmissionStrategy submissionStrategy,

                                    @NonNull
                                    UserProfile userDetails) {
    public static CreateDocumentRequest of(SubmissionStrategy submissionStrategy, UserProfile customUserDetails) {
        return new CreateDocumentRequest(submissionStrategy, customUserDetails);
    }
}