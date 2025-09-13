package lule.dictionary.documents.data.attribute;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.documentSubmission.SubmissionStrategy;

import java.util.Map;

public record DocumentFormAttribute(@NonNull SubmissionStrategy submissionStrategy,
                                    @NonNull Map<DocumentLocalizationKey, String> localization) {
    public static DocumentFormAttribute of(SubmissionStrategy submissionStrategy, Map<DocumentLocalizationKey, String> localization) {
        return new DocumentFormAttribute(submissionStrategy, localization);
    }
}
