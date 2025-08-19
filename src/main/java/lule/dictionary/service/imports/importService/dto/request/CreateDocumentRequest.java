package lule.dictionary.service.imports.importService.dto.request;

import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.imports.dto.SubmissionStrategy;

public record CreateDocumentRequest(@NonNull
                                    SubmissionStrategy submissionStrategy,

                                    @NonNull
                                    CustomUserDetails userDetails) {
    public static CreateDocumentRequest of(SubmissionStrategy submissionStrategy, CustomUserDetails customUserDetails) {
        return new CreateDocumentRequest(submissionStrategy, customUserDetails);
    }
}