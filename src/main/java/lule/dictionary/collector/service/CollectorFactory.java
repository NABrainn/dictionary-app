package lule.dictionary.collector.service;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.documents.data.request.loadDocument.SelectedPhraseInfo;
import lule.dictionary.documents.data.request.loadDocument.SelectedWordInfo;
import lule.dictionary.translations.data.entity.Translation;
import lule.dictionary.translations.data.entity.UninitializedPhrase;
import lule.dictionary.translations.data.entity.UninitializedTranslation;
import lule.dictionary.userProfiles.data.OwnerInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CollectorFactory {
    public Collector<DocumentUnit, ParagraphStore, ParagraphStore> toParagraphs() {
        return Collector.of(
                () -> ParagraphStore.of(new ArrayList<>(), new ArrayList<>(), new AtomicInteger(0)),
                (store, unit) -> {
                    store.units().add(unit);
                    if(store.units().getLast().rawText().contains("\n")) {
                        int firstId = 0;
                        int lastId = store.units().size() - 1;
                        List<DocumentUnit> paragraphUnits = new ArrayList<>(store.units().subList(firstId, lastId));
                        store.paragraphs().add(Paragraph.of(store.counter().getAndIncrement(), paragraphUnits));
                        store.units().subList(firstId, lastId).clear();
                    }
                },
                (left, right) -> {
                    left.paragraphs().addAll(right.paragraphs());
                    return left;
                },
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

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

    public Collector<WordUnit, DocumentUnitStore, DocumentUnitStore> toDocumentUnits(@NonNull Phrases phrases,
                                                                                     @NonNull SelectedWordInfo selectedWordInfo) {
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
                            store.phraseParts().clear();
                        }
                    }
                    else {
                        store.phraseParts().clear();
                    }
                    if(!store.selected().get()) {
                        DocumentUnit last = store.documentUnits().getLast();
                        if(last.id() == selectedWordInfo.startId()) {
                            store.documentUnits().removeLast();
                            SelectedUnit selectedUnitToAdd = SelectedWordUnit.of(
                                    selectedWordInfo.startId(),
                                    !(last.translation() instanceof UninitializedTranslation),
                                    last.translation(),
                                    last.rawText()
                            );
                            store.documentUnits().add(selectedUnitToAdd);
                            store.selected().set(true);
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

    public Collector<WordUnit, DocumentUnitStore, DocumentUnitStore> toDocumentUnits(@NonNull Phrases phrases,
                                                                                     @NonNull SelectedPhraseInfo selectedPhraseInfo) {
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
                            store.phraseParts().clear();
                        }
                    }
                    else {
                        store.phraseParts().clear();
                    }
                    if(!store.selected().get()) {
                        if(store.documentUnits().getLast() instanceof PhraseUnit phraseUnit) {
                            if(phraseUnit.id() == selectedPhraseInfo.startId()) {
                                DocumentUnit toRemove = store.documentUnits().removeLast();
                                SelectedUnit selectedUnitToAdd = SelectedPhraseUnit.of(
                                        toRemove.id(),
                                        toRemove.id(),
                                        true,
                                        toRemove.translation(),
                                        toRemove.rawText()
                                );
                                store.documentUnits().add(selectedUnitToAdd);
                                store.selected().set(true);
                            }
                        }
                        else if(store.documentUnits().getLast() instanceof WordUnit wUnit) {
                            if(wUnit.id() == selectedPhraseInfo.endId()) {
                                System.out.println(store.documentUnits());
                                List<DocumentUnit> toRemove = store.documentUnits().subList(
                                        store.documentUnits().size() - (selectedPhraseInfo.endId() - selectedPhraseInfo.startId() + 1),
                                        store.documentUnits().size()
                                );
                                String targetWord = toRemove.stream()
                                        .map(unit -> unit.translation().processedTargetWord())
                                        .collect(Collectors.joining(" "));
                                OwnerInfo ownerInfo = toRemove.getFirst().translation().ownerInfo();
                                String rawText = toRemove.stream()
                                        .map(DocumentUnit::rawText)
                                        .collect(Collectors.joining(" "));
                                SelectedUnit selectedUnitToAdd = SelectedPhraseUnit.of(
                                        selectedPhraseInfo.startId(),
                                        selectedPhraseInfo.endId(),
                                        false,
                                        UninitializedPhrase.of(targetWord, ownerInfo),
                                        rawText
                                );
                                toRemove.clear();
                                store.documentUnits().add(selectedUnitToAdd);
                                store.selected().set(true);
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
