package lule.dictionary.documents.data.attribute;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.documentSubmission.DocumentFormType;

import java.util.Map;

public record DocumentFormAttribute(@NonNull DocumentFormType documentFormType,
                                    @NonNull Map<DocumentLocalizationKey, String> localization) {
    public static DocumentFormAttribute of(DocumentFormType documentFormType, Map<DocumentLocalizationKey, String> localization) {
        return new DocumentFormAttribute(documentFormType, localization);
    }
}
