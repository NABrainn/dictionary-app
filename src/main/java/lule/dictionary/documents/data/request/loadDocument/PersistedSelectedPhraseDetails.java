package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record PersistedSelectedPhraseDetails(int startId,
                                             int endId,
                                             @NonNull String phraseText) implements SelectedPhraseDetails {
    public static PersistedSelectedPhraseDetails of(int startId, int endId, String phraseText) {
        return new PersistedSelectedPhraseDetails(startId, endId, phraseText);
    }
}
