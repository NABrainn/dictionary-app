package lule.dictionary.translations.data.entity;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.userProfiles.data.OwnerInfo;

import java.util.List;

@With
public record UninitializedWordTranslation(@NonNull List<String> sourceWords,
                                           @NonNull String processedTargetWord,
                                           @NonNull Familiarity familiarity,
                                           @NonNull OwnerInfo ownerInfo) implements UninitializedTranslation {
    public static UninitializedWordTranslation of(String processedTargetWord,
                                                  OwnerInfo ownerInfo) {
        return new UninitializedWordTranslation(List.of(), processedTargetWord, Familiarity.UNKNOWN, ownerInfo);
    }
    public static UninitializedWordTranslation of(List<String> sourceWords,
                                                  String processedTargetWord,
                                                  OwnerInfo ownerInfo) {
        return new UninitializedWordTranslation(sourceWords, processedTargetWord, Familiarity.UNKNOWN, ownerInfo);
    }
}