package lule.dictionary.documents.service;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.documents.data.request.loadDocument.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;

@Service
public class DocumentUnitCollectors {
    public Collector<DocumentUnit, ParagraphStore, ParagraphStore> toParagraphs() {
        return Collector.of(
                () -> ParagraphStore.of(new ArrayList<>(), new ArrayList<>(), new AtomicInteger(0)),
                ParagraphStore::extractParagraphs,
                (left, right) -> {
                    left.paragraphs().addAll(right.paragraphs());
                    return left;
                },
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

    public Collector<WordUnit, DocumentUnitStore, DocumentUnitStore> toDocumentUnits(@NonNull Phrases phrases) {
        return Collector.of(
                () -> DocumentUnitStore.of(new ArrayList<>(), new ArrayList<>()),
                (store, wordUnit) -> store.gatherDocumentUnits(wordUnit, phrases),
                (left, right) -> {
                    List<DocumentUnit> leftUnits = left.documentUnits();
                    List<DocumentUnit> rightUnits = right.documentUnits();
                    leftUnits.addAll(rightUnits);
                    return left;
                },
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

    public Collector<WordUnit, DocumentUnitStore, DocumentUnitStore> toDocumentUnits(@NonNull Phrases phrases,
                                                                                     @NonNull SelectedWordDetails selectedWordCords) {
        return Collector.of(
                () -> DocumentUnitStore.of(new ArrayList<>(), new ArrayList<>(), new AtomicBoolean(false)),
                (store, wordUnit) -> {
                    store.gatherDocumentUnits(wordUnit, phrases);
                    store.extractSelectedWord(selectedWordCords);
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

    public Collector<WordUnit, DocumentUnitStore, DocumentUnitStore> toDocumentUnits(@NonNull Phrases phrases,
                                                                                     @NonNull SelectedPhraseDetails selectedPhraseCords) {
        return Collector.of(
                () -> DocumentUnitStore.of(new ArrayList<>(), new ArrayList<>(), new AtomicBoolean(false)),
                (store, wordUnit) -> {
                    store.gatherDocumentUnits(wordUnit, phrases);
                    store.extractSelectedPhrase(selectedPhraseCords, selectedPhraseCords.phraseText());
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
