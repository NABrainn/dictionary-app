package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record UninitializedSelectedPhraseDetails(int startId,
                                                 int endId,
                                                 @NonNull String phraseText) implements SelectedPhraseDetails {
    public static SelectedUnitDetails of(int startId,
                                         int endId,
                                         String phraseText) {
        return new UninitializedSelectedPhraseDetails(startId, endId, phraseText);
    }
}
