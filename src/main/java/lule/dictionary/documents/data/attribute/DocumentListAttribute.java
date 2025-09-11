package lule.dictionary.documents.data.attribute;

import lombok.NonNull;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;

import java.util.List;
import java.util.Map;

public record DocumentListAttribute(@NonNull List<DocumentWithTranslationData> documents,
                                    @NonNull Map<DocumentLocalizationKey, String> localization) {
    public static DocumentListAttribute of(List<DocumentWithTranslationData> documents, Map<DocumentLocalizationKey, String> localization) {
        return new DocumentListAttribute(documents, localization);
    }
}
