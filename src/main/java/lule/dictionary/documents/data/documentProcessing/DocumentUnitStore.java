package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.documents.data.request.loadDocument.PersistedSelectedPhraseCords;
import lule.dictionary.documents.data.request.loadDocument.SelectedPhraseCords;
import lule.dictionary.documents.data.request.loadDocument.SelectedWordCords;
import lule.dictionary.documents.data.request.loadDocument.UninitializedSelectedPhraseCords;
import lule.dictionary.translations.data.entity.*;
import lule.dictionary.userProfiles.data.OwnerInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    public void gatherDocumentUnits(@NonNull WordUnit wordUnit,
                                    @NonNull Phrases phrases) {
        this.documentUnits().add(wordUnit);
        this.idCounter().incrementAndGet();
        if(wordUnit.isPhrasePart()) {
            this.phraseParts().add(wordUnit);
            String normalizedPhrase = this.phraseParts().stream()
                    .map(unit -> unit.translation().processedTargetWord())
                    .collect(Collectors.joining(" "));
            String rawPhrase = this.phraseParts().stream()
                    .map(DocumentUnit::rawText)
                    .collect(Collectors.joining(" "));
            if(phrases.containsPhrase(normalizedPhrase).isPresent()) {
                List<DocumentUnit> toRemove = this.documentUnits().subList(this.documentUnits().size() - this.phraseParts().size(), this.documentUnits().size());
                int firstElementId = toRemove.getFirst().id();
                toRemove.clear();
                Translation foundPhrase = phrases.containsPhrase(normalizedPhrase).get();
                PhraseUnit phraseToAdd = PhraseUnit.of(
                        firstElementId,
                        foundPhrase,
                        rawPhrase,
                        this.phraseParts().size()
                );
                this.documentUnits().add(phraseToAdd);
                this.phraseParts().clear();
            }
        }
        else {
            this.phraseParts().clear();
        }
    }

    public void extractSelectedPhrase(SelectedPhraseCords selectedPhraseCords) {
        if(!this.selected().get()) {
            switch (this.documentUnits().getLast()) {
                case PhraseUnit phraseUnit -> {
                    if(phraseUnit.id() == selectedPhraseCords.startId()) {
                        DocumentUnit toRemove = this.documentUnits().removeLast();
                        SelectedUnit selectedUnitToAdd = SelectedPhraseUnit.of(
                                selectedPhraseCords.startId(),
                                switch (selectedPhraseCords) {
                                    case PersistedSelectedPhraseCords ignored -> true;
                                    case UninitializedSelectedPhraseCords ignored -> false;
                                },
                                toRemove.translation(),
                                toRemove.rawText()
                        );
                        this.documentUnits().add(selectedUnitToAdd);
                        this.selected().set(true);
                    }
                }
                case WordUnit wUnit -> {
                    if(wUnit.id() == selectedPhraseCords.endId()) {
                        List<DocumentUnit> toRemove = this.documentUnits().subList(
                                this.documentUnits().size() - (selectedPhraseCords.endId() - selectedPhraseCords.startId() + 1),
                                this.documentUnits().size()
                        );
                        String targetWord = toRemove.stream()
                                .map(unit -> unit.translation().processedTargetWord())
                                .collect(Collectors.joining(" "));
                        OwnerInfo ownerInfo = toRemove.getFirst().translation().ownerInfo();
                        String rawText = toRemove.stream()
                                .map(DocumentUnit::rawText)
                                .collect(Collectors.joining(" "));
                        SelectedUnit selectedUnitToAdd = SelectedPhraseUnit.of(
                                selectedPhraseCords.startId(),
                                switch (selectedPhraseCords) {
                                    case PersistedSelectedPhraseCords ignored -> true;
                                    case UninitializedSelectedPhraseCords ignored -> false;
                                },
                                UninitializedPhrase.of(targetWord, ownerInfo),
                                rawText
                        );
                        toRemove.clear();
                        this.documentUnits().add(selectedUnitToAdd);
                        this.selected().set(true);
                    }
                }
                case SelectedUnit ignored -> {}
            }
        }
    }

    public void extractSelectedWord(@NonNull SelectedWordCords selectedWordCords) {
        if(!this.selected().get()) {
            DocumentUnit last = this.documentUnits().getLast();
            if(last.id() == selectedWordCords.startId()) {
                this.documentUnits().removeLast();
                SelectedUnit selectedUnitToAdd = SelectedWordUnit.of(
                        selectedWordCords.startId(),
                        switch (last.translation()) {
                            case Phrase ignored1 -> true;
                            case UninitializedTranslation ignored -> false;
                            case Word ignored -> true;
                        },
                        last.translation(),
                        last.rawText()
                );
                this.documentUnits().add(selectedUnitToAdd);
                this.selected().set(true);
            }
        }
    }
}
