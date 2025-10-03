package lule.dictionary.documents.data.parseDocument;

import lombok.NonNull;

public record ParseWithoutSelection(@NonNull TranslationInfo translationInfo,
                                    @NonNull OwnerInfo ownerInfo,
                                    @NonNull String contentBlob) implements ParseDocument {
    public static ParseWithoutSelection of(TranslationInfo translationInfo, OwnerInfo ownerInfo, String contentBlob) {
        return new ParseWithoutSelection(translationInfo, ownerInfo, contentBlob);
    }
}
