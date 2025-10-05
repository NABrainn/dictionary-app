package lule.dictionary.documents.data.parseDocument;

import lombok.NonNull;
import lule.dictionary.documents.data.request.loadDocument.SelectedPhraseDetails;
import lule.dictionary.userProfiles.data.OwnerInfo;

public record ParseWithPhraseSelection(@NonNull TranslationInfo translationInfo,
                                       @NonNull OwnerInfo ownerInfo,
                                       @NonNull String contentBlob,
                                       @NonNull SelectedPhraseDetails selectedPhraseInfo) implements ParseDocument {
    public static ParseWithPhraseSelection of(TranslationInfo translationInfo, OwnerInfo ownerInfo, String contentBlob, SelectedPhraseDetails selectedPhraseInfo) {
        return new ParseWithPhraseSelection(translationInfo, ownerInfo, contentBlob, selectedPhraseInfo);
    }
}
