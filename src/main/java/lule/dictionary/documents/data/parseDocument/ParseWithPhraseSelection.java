package lule.dictionary.documents.data.parseDocument;

import lombok.NonNull;
import lule.dictionary.documents.data.request.loadDocument.SelectedPhraseInfo;

public record ParseWithPhraseSelection(@NonNull TranslationInfo translationInfo,
                                       @NonNull OwnerInfo ownerInfo,
                                       @NonNull String contentBlob,
                                       @NonNull SelectedPhraseInfo selectedPhraseInfo) implements ParseDocument {
    public static ParseWithPhraseSelection of(TranslationInfo translationInfo, OwnerInfo ownerInfo, String contentBlob, SelectedPhraseInfo selectedPhraseInfo) {
        return new ParseWithPhraseSelection(translationInfo, ownerInfo, contentBlob, selectedPhraseInfo);
    }
}
