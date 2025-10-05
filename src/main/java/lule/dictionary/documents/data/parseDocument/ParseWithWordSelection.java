package lule.dictionary.documents.data.parseDocument;

import lombok.NonNull;
import lule.dictionary.documents.data.request.loadDocument.SelectedUnitCords;
import lule.dictionary.documents.data.request.loadDocument.SelectedWordCords;
import lule.dictionary.userProfiles.data.OwnerInfo;

public record ParseWithWordSelection(@NonNull TranslationInfo translationInfo,
                                     @NonNull OwnerInfo ownerInfo,
                                     @NonNull String contentBlob,
                                     @NonNull SelectedWordCords selectedWordInfo) implements ParseDocument {
    public static ParseWithWordSelection of(TranslationInfo translationInfo, OwnerInfo ownerInfo, String contentBlob, SelectedWordCords selectedWordInfo) {
        return new ParseWithWordSelection(translationInfo, ownerInfo, contentBlob, selectedWordInfo);
    }
}
