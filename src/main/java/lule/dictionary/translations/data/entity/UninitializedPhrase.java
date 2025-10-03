package lule.dictionary.translations.data.entity;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.userProfiles.data.OwnerInfo;

import java.util.List;

@With
public record UninitializedPhrase(@NonNull List<String> sourceWords,
                                  @NonNull String processedTargetWord,
                                  @NonNull Familiarity familiarity,
                                  @NonNull OwnerInfo ownerInfo) implements UninitializedTranslation {
    public static UninitializedPhrase of(String processedTargetWord,
                                         OwnerInfo ownerInfo) {
        return new UninitializedPhrase(List.of(), processedTargetWord, Familiarity.UNKNOWN, ownerInfo);
    }
    public static UninitializedPhrase of(List<String> sourceWords,
                                         String processedTargetWord,
                                         OwnerInfo ownerInfo) {
        return new UninitializedPhrase(sourceWords, processedTargetWord, Familiarity.UNKNOWN, ownerInfo);
    }
}
