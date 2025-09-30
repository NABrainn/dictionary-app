package lule.dictionary.collector.service;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.DocumentUnit;
import lule.dictionary.documents.data.documentProcessing.DocumentUnitStore;
import lule.dictionary.documents.data.documentProcessing.NonPhraseUnit;
import lule.dictionary.documents.data.documentProcessing.Phrases;
import lule.dictionary.translations.data.Translation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

@Service
public class CollectorFactory {
    public Collector<NonPhraseUnit, DocumentUnitStore, DocumentUnitStore> documentUnitCollector(@NonNull Phrases phrases) {
        return Collector.of(
                () -> DocumentUnitStore.of(new ArrayList<>(), new ArrayList<>()),
                (store, nonPhraseUnit) -> {
                    store.addDocumentUnit((DocumentUnit) nonPhraseUnit);
                    if (!nonPhraseUnit.isPhrasePart()) {
                        store.clearPhraseParts();
                    }
                    else {
                        store.addPhrasePart((DocumentUnit) nonPhraseUnit);
                    }
                    if (phrases.findPhrase(store.bufferValue()).isPresent()) {
                        Translation translation = phrases.findPhrase(store.bufferValue()).get();
                        store.wrapToPhrase(translation);
                        store.clearPhraseParts();
                    }
                },
                (left, right) -> {
                    List<DocumentUnit> leftUnits = left.documentUnits();
                    List<DocumentUnit> rightUnits = right.documentUnits();
                    leftUnits.addAll(rightUnits);
                    return left;
                },
                Collector.Characteristics.IDENTITY_FINISH
        );
    }
}
