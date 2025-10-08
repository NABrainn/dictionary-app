package lule.dictionary.translations.data.entity;

import lombok.With;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.userProfiles.data.OwnerInfo;

import java.util.List;

@With
public record EmptyTranslation(List<String> sourceWords,
                               String processedTargetWord,
                               Familiarity familiarity,
                               OwnerInfo ownerInfo) implements Translation {
    public static EmptyTranslation of() {
        return new EmptyTranslation(List.of(), "", Familiarity.UNKNOWN, null);
    }
}
