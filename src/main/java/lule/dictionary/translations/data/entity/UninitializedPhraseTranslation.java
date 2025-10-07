package lule.dictionary.translations.data.entity;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.userProfiles.data.OwnerInfo;

import java.util.List;

@With
public record UninitializedPhraseTranslation(@NonNull List<String> sourceWords,
                                             @NonNull String processedTargetWord,
                                             @NonNull Familiarity familiarity,
                                             @NonNull OwnerInfo ownerInfo) implements UninitializedTranslation {
    public static UninitializedPhraseTranslation of(String processedTargetWord,
                                                    OwnerInfo ownerInfo) {
        return new UninitializedPhraseTranslation(List.of(), processedTargetWord, Familiarity.UNKNOWN, ownerInfo);
    }
    public static UninitializedPhraseTranslation of(List<String> sourceWords,
                                                    String processedTargetWord,
                                                    OwnerInfo ownerInfo) {
        return new UninitializedPhraseTranslation(sourceWords, processedTargetWord, Familiarity.UNKNOWN, ownerInfo);
    }
}
