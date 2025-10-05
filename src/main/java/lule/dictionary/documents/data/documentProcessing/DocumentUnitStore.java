package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.documents.data.request.loadDocument.PersistedSelectedPhraseDetails;
import lule.dictionary.documents.data.request.loadDocument.SelectedPhraseDetails;
import lule.dictionary.documents.data.request.loadDocument.SelectedWordDetails;
import lule.dictionary.documents.data.request.loadDocument.UninitializedSelectedPhraseDetails;
import lule.dictionary.translations.data.entity.*;
import lule.dictionary.userProfiles.data.OwnerInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public record DocumentUnitStore(@NonNull List<WordUnit> phraseParts,
                                @NonNull List<DocumentUnit> documentUnits,
                                AtomicBoolean selected) {
    public static DocumentUnitStore of(List<WordUnit> phrasesInARow, List<DocumentUnit> documentUnits, AtomicBoolean selected) {
        return new DocumentUnitStore(phrasesInARow, documentUnits, selected);
    }
    public static DocumentUnitStore of(List<WordUnit> phrasesInARow, List<DocumentUnit> documentUnits) {
        return new DocumentUnitStore(phrasesInARow, documentUnits, null);
    }

    public void gatherDocumentUnits(@NonNull WordUnit wordUnit,
                                    @NonNull Phrases phrases) {
        documentUnits().add(wordUnit);
        if(wordUnit.isPhrasePart()) {
            phraseParts().add(wordUnit);
            String normalizedPhrase = phraseParts().stream()
                    .map(unit -> unit.translation().processedTargetWord())
                    .collect(Collectors.joining(" "));
            String rawPhraseText = phraseParts().stream()
                    .map(DocumentUnit::rawText)
                    .collect(Collectors.joining(" "));
            if(phrases.containsPhrase(normalizedPhrase).isPresent()) {
                int startId = documentUnits().size() - phraseParts().size();
                int endId = documentUnits().size();
                List<DocumentUnit> toRemove = documentUnits().subList(startId, endId);
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
                documentUnits().add(phraseToAdd);
            }
        }
        else {
            phraseParts().clear();
        }
    }
    public void gatherDocumentUnits(@NonNull WordUnit wordUnit,
                                    @NonNull Phrases phrases,
                                    @NonNull String selectedPhraseText) {
        documentUnits().add(wordUnit);
        if(wordUnit.isPhrasePart()) {
            phraseParts().add(wordUnit);
            String normalizedPhrase = this.phraseParts().stream()
                    .map(unit -> unit.translation().processedTargetWord())
                    .collect(Collectors.joining(" "));
            String rawPhraseText = phraseParts().stream()
                    .map(DocumentUnit::rawText)
                    .collect(Collectors.joining(" "));
            if(phrases.containsPhrase(normalizedPhrase).isPresent()) {
                int stratId = documentUnits().size() - phraseParts().size();
                int endId = documentUnits().size();
                List<DocumentUnit> toRemove = documentUnits().subList(stratId, endId);
                int id = toRemove.getFirst().id();
                Translation translation = phrases.containsPhrase(normalizedPhrase).get();
                PhraseUnit phraseToAdd = PhraseUnit.of(
                        id,
                        translation,
                        rawPhraseText,
                        phraseParts().size()
                );
                toRemove.clear();
                this.phraseParts().clear();
                this.documentUnits().add(phraseToAdd);
            }
        }
        else {
            this.phraseParts().clear();
        }
    }

    public void extractSelectedPhrase(@NonNull SelectedPhraseDetails selectedPhraseDetails,
                                      @NonNull String selectedPhraseText) {
            switch (this.documentUnits().getLast()) {
                case PhraseUnit phraseUnit -> {
                    if (!this.selected().get()) {
                        if (phraseUnit.id() == selectedPhraseDetails.startId()) {
                            DocumentUnit toRemove = this.documentUnits().removeLast();
                            SelectedUnit selectedUnitToAdd = SelectedPhraseUnit.of(
                                    selectedPhraseDetails.startId(),
                                    switch (selectedPhraseDetails) {
                                        case PersistedSelectedPhraseDetails ignored -> true;
                                        case UninitializedSelectedPhraseDetails ignored -> false;
                                    },
                                    toRemove.translation(),
                                    toRemove.rawText()
                            );
                            this.documentUnits().add(selectedUnitToAdd);
                            this.selected().set(true);
                        }
                    }
                }
                case WordUnit wUnit -> {
                    int phraseLength = selectedPhraseText.split(" ").length;
                    DocumentUnit last = documentUnits().getLast();
                    if (wUnit.id() != selectedPhraseDetails.endId() && last.id() >= phraseLength) {
                        int startId = documentUnits().size() - 2;
                        int endId = documentUnits().size();
                        List<DocumentUnit> matchingPhrase = documentUnits().subList(startId, endId);
                        String matchingPhraseText = matchingPhrase
                                .stream()
                                .map(unit -> unit.translation().processedTargetWord())
                                .collect(Collectors.joining(" "));
                        if (matchingPhraseText.equals(selectedPhraseText)) {
                            String matchingPhraseRawText = matchingPhrase.stream()
                                    .map(DocumentUnit::rawText)
                                    .collect(Collectors.joining(" "));
                            PhraseUnit phraseToAdd = PhraseUnit.of(
                                    matchingPhrase.getFirst().id(),
                                    UninitializedPhrase.of(List.of(), matchingPhraseText, matchingPhrase.getFirst().translation().ownerInfo()),
                                    matchingPhraseRawText,
                                    matchingPhrase.size()
                            );
                            matchingPhrase.clear();
                            documentUnits().add(phraseToAdd);
                        }
                    }
                    else if(wUnit.id() == selectedPhraseDetails.endId()) {
                        if (!this.selected().get()) {
                            int startId = documentUnits().size() - (selectedPhraseDetails.endId() - selectedPhraseDetails.startId() + 1);
                            int endId = documentUnits().size();
                            List<DocumentUnit> toRemove = this.documentUnits().subList(startId, endId);
                            String targetWord = toRemove.stream()
                                    .map(unit -> unit.translation().processedTargetWord())
                                    .collect(Collectors.joining(" "));
                            OwnerInfo ownerInfo = toRemove.getFirst().translation().ownerInfo();
                            String rawText = toRemove.stream()
                                    .map(DocumentUnit::rawText)
                                    .collect(Collectors.joining(" "));
                            SelectedUnit selectedUnitToAdd = SelectedPhraseUnit.of(
                                    selectedPhraseDetails.startId(),
                                    switch (selectedPhraseDetails) {
                                        case PersistedSelectedPhraseDetails ignored -> true;
                                        case UninitializedSelectedPhraseDetails ignored -> false;
                                    },
                                    UninitializedPhrase.of(targetWord, ownerInfo),
                                    rawText
                            );
                            toRemove.clear();
                            this.documentUnits().add(selectedUnitToAdd);
                            this.selected().set(true);
                        }
                    }
                }
                case SelectedUnit ignored -> {}
        }
    }

    public void extractSelectedWord(@NonNull SelectedWordDetails selectedWordCords) {
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
