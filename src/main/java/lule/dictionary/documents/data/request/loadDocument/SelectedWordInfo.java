package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record SelectedWordInfo(@NonNull String text,
                               boolean isPersisted,
                               int startId) implements SelectedUnitInfo {
    public static SelectedWordInfo of(String text, boolean isPersisted, int startId) {
        return new SelectedWordInfo(text, isPersisted, startId);
    }
}
