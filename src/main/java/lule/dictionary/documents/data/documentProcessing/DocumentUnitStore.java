package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public record DocumentUnitStore(@NonNull List<WordUnit> phraseParts,
                                @NonNull List<DocumentUnit> documentUnits,
                                @NonNull AtomicInteger idCounter,
                                AtomicBoolean selected) {
    public static DocumentUnitStore of(List<WordUnit> phrasesInARow, List<DocumentUnit> documentUnits, AtomicInteger idCounter) {
        return new DocumentUnitStore(phrasesInARow, documentUnits, idCounter, null);
    }
    public static DocumentUnitStore of(List<WordUnit> phrasesInARow, List<DocumentUnit> documentUnits, AtomicInteger idCounter, AtomicBoolean selected) {
        return new DocumentUnitStore(phrasesInARow, documentUnits, idCounter, selected);
    }
}
