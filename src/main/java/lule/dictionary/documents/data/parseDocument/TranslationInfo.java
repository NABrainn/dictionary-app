package lule.dictionary.documents.data.parseDocument;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.Phrases;
import lule.dictionary.translations.data.entity.Translation;

import java.util.Map;

public record TranslationInfo(@NonNull Map<String, Translation> translations,
                              @NonNull Phrases phrases) {
    public static TranslationInfo of(Map<String, Translation> translations,
                                     Phrases phrases) {
        return new TranslationInfo(translations, phrases);
    }
}
