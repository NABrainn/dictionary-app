package lule.dictionary.userProfiles.data;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record OwnerInfo(@NonNull Language sourceLanguage,
                        @NonNull Language targetLanguage,
                        @NonNull String owner) {
    public static OwnerInfo of(Language sourceLanguage, Language targetLanguage, String owner) {
        return new OwnerInfo(sourceLanguage, targetLanguage, owner);
    }
}
