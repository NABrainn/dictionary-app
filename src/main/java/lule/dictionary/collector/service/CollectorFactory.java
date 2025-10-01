package lule.dictionary.collector.service;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.translations.data.Translation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CollectorFactory {
    public Collector<WordUnit, DocumentUnitStore, DocumentUnitStore> toDocumentUnits(@NonNull Phrases phrases) {
        return Collector.of(
                () -> DocumentUnitStore.of(new ArrayList<>(), new ArrayList<>(), new AtomicInteger(0)),
                (store, wordUnit) -> {
                    store.documentUnits().add(wordUnit.withId(store.idCounter().getAndIncrement()));
                    if(wordUnit.isPhrasePart()) {
                        store.phraseParts().add(wordUnit);
                        String normalizedPhrase = store.phraseParts()
                                .stream()
                                .map(unit -> unit.translation().processedTargetWord())
                                .collect(Collectors.joining(" "));
                        String rawPhrase = store.phraseParts().stream()
                                .map(DocumentUnit::rawText)
                                .collect(Collectors.joining(" "));
                        if(phrases.containsPhrase(normalizedPhrase).isPresent()) {
                            List<DocumentUnit> toRemove = store.documentUnits().subList(store.documentUnits().size() - store.phraseParts().size(), store.documentUnits().size());
                            int firstElementId = toRemove.getFirst().id();
                            toRemove.clear();
                            Translation foundPhrase = phrases.containsPhrase(normalizedPhrase).get();
                            PhraseUnit phraseToAdd = PhraseUnit.of(firstElementId, foundPhrase, rawPhrase, store.phraseParts().size());
                            store.documentUnits().add(phraseToAdd);
                            store.idCounter().set(store.idCounter().get() - (phraseToAdd.size() - 1));
                            store.phraseParts().clear();
                        }
                    }
                    else {
                        store.phraseParts().clear();
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
    public Collector<WordUnit, DocumentUnitStore, DocumentUnitStore> toDocumentUnits(@NonNull Phrases phrases, int startId, int length) {
        return Collector.of(
                () -> DocumentUnitStore.of(new ArrayList<>(), new ArrayList<>(), new AtomicInteger(0), new AtomicBoolean(false)),
                (store, wordUnit) -> {
                    store.documentUnits().add(wordUnit.withId(store.idCounter().getAndIncrement()));
                    if(wordUnit.isPhrasePart()) {
                        store.phraseParts().add(wordUnit);
                        String normalizedPhrase = store.phraseParts().stream()
                                .map(unit -> unit.translation().processedTargetWord())
                                .collect(Collectors.joining(" "));
                        String rawPhrase = store.phraseParts().stream()
                                .map(DocumentUnit::rawText)
                                .collect(Collectors.joining(" "));
                        if(phrases.containsPhrase(normalizedPhrase).isPresent()) {
                            List<DocumentUnit> toRemove = store.documentUnits().subList(store.documentUnits().size() - store.phraseParts().size(), store.documentUnits().size());
                            int firstElementId = toRemove.getFirst().id();
                            toRemove.clear();
                            Translation foundPhrase = phrases.containsPhrase(normalizedPhrase).get();
                            PhraseUnit phraseToAdd = PhraseUnit.of(firstElementId, foundPhrase, rawPhrase, store.phraseParts().size());
                            store.documentUnits().add(phraseToAdd);
                            store.idCounter().set(store.idCounter().get() - (phraseToAdd.size() - 1));
                            store.phraseParts().clear();
                        }
                    }
                    else {
                        store.phraseParts().clear();
                    }
                    if(!store.selected().get()) {
                        if(length == 1) {
                            store.documentUnits().getLast();
                            DocumentUnit toRemove = store.documentUnits().removeLast();
                            SelectedUnit selectedUnitToAdd = SelectedWordUnit.of(toRemove.id(), toRemove.translation(), toRemove.rawText());
                            store.documentUnits().add(selectedUnitToAdd);
                            store.selected().set(true);
                        }
                        else {
                            if(store.documentUnits().getLast() instanceof PhraseUnit phraseUnit) {
                                if(phraseUnit.id() == startId) {
                                    DocumentUnit toRemove = store.documentUnits().removeLast();
                                    SelectedUnit selectedUnitToAdd = SelectedPhraseUnit.of(toRemove.id(), toRemove.translation(), toRemove.rawText());
                                    store.documentUnits().add(selectedUnitToAdd);
                                    store.selected().set(true);
                                }
                            }
                            else if(store.documentUnits().getLast() instanceof WordUnit wUnit) {
                                if(wUnit.id() == startId + length - 1) {
                                    List<DocumentUnit> toRemove = store.documentUnits().subList(startId, startId + length);
                                    SelectedUnit selectedUnitToAdd = SelectedPhraseUnit.of(
                                            toRemove.getFirst().id(),
                                            Translation.nonTranslation(
                                                    toRemove.stream()
                                                            .map(unit -> unit.translation().processedTargetWord())
                                                            .collect(Collectors.joining(" ")),
                                                    toRemove.getFirst().translation().sourceLanguage(),
                                                    toRemove.getFirst().translation().targetLanguage(),
                                                    toRemove.getFirst().translation().owner()
                                                ),
                                            toRemove.stream()
                                                    .map(DocumentUnit::rawText)
                                                    .collect(Collectors.joining(" "))
                                    );
                                    toRemove.clear();
                                    store.documentUnits().add(selectedUnitToAdd);
                                    store.selected().set(true);
                                }
                            }
                        }
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
