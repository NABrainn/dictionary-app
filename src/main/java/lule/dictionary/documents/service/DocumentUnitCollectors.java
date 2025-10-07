package lule.dictionary.documents.service;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.documents.data.documentProcessing.collectorStore.*;
import lule.dictionary.documents.data.request.loadDocument.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;

@Service
public class DocumentUnitCollectors {
    public Collector<DocumentUnit, ParagraphStore, ParagraphStore> toParagraphs() {
        return Collector.of(
                () -> ParagraphStore.of(new ArrayList<>(), new ArrayList<>(), new AtomicInteger(0)),
                ParagraphStore::accumulate,
                ParagraphStore::combine,
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

    public Collector<WordUnit, DocumentUnitStore, DocumentUnitStore> toDocumentUnits(@NonNull Phrases phrases) {
        return Collector.of(
                () -> DocumentUnitStore.of(new ArrayList<>(), new ArrayList<>()),
                (store, wordUnit) -> store.accumulate(wordUnit, phrases),
                DocumentUnitStore::combine,
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

    public Collector<DocumentUnit, SelectedWordStore, SelectedWordStore> toWordSelection(@NonNull SelectedWordDetails selectedWordDetails) {
        return Collector.of(
                () -> SelectedWordStore.of(new ArrayList<>()),
                (store, unit) -> store.accumulate(unit, selectedWordDetails),
                SelectedWordStore::combine,
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

    public Collector<DocumentUnit, NewSelectedPhraseStore, NewSelectedPhraseStore> toNewPhraseSelection(@NonNull SelectedPhraseDetails selectedPhraseDetails) {
        return Collector.of(
                () -> NewSelectedPhraseStore.of(new ArrayList<>(), new ArrayList<>()),
                (store, unit) -> store.accumulate(unit, selectedPhraseDetails),
                NewSelectedPhraseStore::combine,
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

    public Collector<DocumentUnit, ExistingSelectedPhraseStore, ExistingSelectedPhraseStore> toExistingPhraseSelection(@NonNull SelectedPhraseDetails selectedPhraseDetails) {
        return Collector.of(
                () -> ExistingSelectedPhraseStore.of(new ArrayList<>()),
                (store, unit) -> store.accumulate(unit, selectedPhraseDetails),
                ExistingSelectedPhraseStore::combine,
                Collector.Characteristics.IDENTITY_FINISH
        );
    }
}
