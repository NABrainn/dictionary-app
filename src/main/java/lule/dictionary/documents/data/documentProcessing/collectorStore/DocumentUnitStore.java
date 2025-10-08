package lule.dictionary.documents.data.documentProcessing.collectorStore;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.DocumentUnit;
import lule.dictionary.documents.data.documentProcessing.PhraseUnit;
import lule.dictionary.documents.data.documentProcessing.Phrases;
import lule.dictionary.documents.data.documentProcessing.WordUnit;
import lule.dictionary.translations.data.entity.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public record DocumentUnitStore(@NonNull List<WordUnit> phraseParts,
                                @NonNull List<DocumentUnit> units,
                                AtomicBoolean selected) {
    public static DocumentUnitStore of(List<WordUnit> phrasesInARow, List<DocumentUnit> units, AtomicBoolean selected) {
        return new DocumentUnitStore(phrasesInARow, units, selected);
    }

    public static DocumentUnitStore of(List<WordUnit> phrasesInARow, List<DocumentUnit> units) {
        return new DocumentUnitStore(phrasesInARow, units, null);
    }

    public void accumulate(@NonNull WordUnit wordUnit,
                           @NonNull Phrases phrases) {
        units().add(wordUnit);
        if (wordUnit.isPhrasePart()) {
            phraseParts().add(wordUnit);
            String normalizedPhrase = phraseParts().stream()
                    .map(unit -> unit.translation().processedTargetWord())
                    .collect(Collectors.joining(" "));
            String rawPhraseText = phraseParts().stream()
                    .map(DocumentUnit::rawText)
                    .collect(Collectors.joining(" "));
            if (phrases.containsPhrase(normalizedPhrase).isPresent()) {
                int startId = units().size() - phraseParts().size();
                int endId = units().size();
                List<DocumentUnit> toRemove = units().subList(startId, endId);
                int id = toRemove.getFirst().id();
                Translation phrase = phrases.containsPhrase(normalizedPhrase).get();
                PhraseUnit phraseToAdd = PhraseUnit.of(
                        id,
                        phrase,
                        rawPhraseText,
                        phraseParts().size()
                );
                toRemove.clear();
                phraseParts().clear();
                units().add(phraseToAdd);
            }
        } else {
            phraseParts().clear();
        }
    }
    public static DocumentUnitStore combine(@NonNull DocumentUnitStore left, @NonNull DocumentUnitStore right) {
        List<DocumentUnit> leftUnits = left.units();
        List<DocumentUnit> rightUnits = right.units();
        leftUnits.addAll(rightUnits);
        return left;
    }
}
