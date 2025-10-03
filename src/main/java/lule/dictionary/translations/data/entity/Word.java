package lule.dictionary.translations.data.entity;

import lombok.NonNull;
import lombok.With;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.userProfiles.data.OwnerInfo;

import java.util.List;

@With
public record Word(@NonNull List<String> sourceWords,
                   @NonNull String processedTargetWord,
                   @NonNull Familiarity familiarity,
                   @NonNull OwnerInfo ownerInfo) implements Translation {
    public static Word of(List<String> sourceWords,
                          String processedTargetWord,
                          Familiarity familiarity,
                          OwnerInfo ownerInfo) {
        return new Word(sourceWords, processedTargetWord, familiarity, ownerInfo);
    }
    @Override
    public boolean equals(Object object) {
        if(object == null) {
            return false;
        }
        if(!(object instanceof Word translation)) {
            return false;
        }
        return this.processedTargetWord.equals(translation.processedTargetWord);
    }
}
