package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record SelectedPhraseInfo(@NonNull String text,
                                 boolean isPersisted,
                                 int startId,
                                 int endId) implements SelectedUnitInfo {
    public static SelectedPhraseInfo of(String text, boolean isPersisted, int startId, int endId) {
        return new SelectedPhraseInfo(text, isPersisted, startId, endId);
    }
}
