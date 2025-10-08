package lule.dictionary.documents.data.documentProcessing.collectorStore;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.DocumentUnit;
import lule.dictionary.documents.data.documentProcessing.PhraseUnit;
import lule.dictionary.documents.data.documentProcessing.SelectedPhraseUnit;
import lule.dictionary.documents.data.documentProcessing.WordUnit;
import lule.dictionary.documents.data.request.loadDocument.SelectedPhraseDetails;
import lule.dictionary.translations.data.entity.UninitializedPhraseTranslation;
import lule.dictionary.translations.data.entity.UninitializedTranslation;
import lule.dictionary.userProfiles.data.OwnerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record NewSelectedPhraseStore(@NonNull ArrayList<DocumentUnit> units,
                                     @NonNull ArrayList<DocumentUnit> buffer) {
    public static NewSelectedPhraseStore of(ArrayList<DocumentUnit> units, ArrayList<DocumentUnit> buffer) {
        return new NewSelectedPhraseStore(units, buffer);
    }

    public void accumulate(DocumentUnit unit, SelectedPhraseDetails selectedPhraseDetails) {
        if(unit instanceof WordUnit wordUnit) {
            if(wordUnit.id() == selectedPhraseDetails.endId()) {
                units.add(unit);
                int startId = units.size() - (selectedPhraseDetails.endId() - selectedPhraseDetails.startId());
                int endId = units.size();
                List<DocumentUnit> toRemove = units.subList(startId - 1, endId);
                List<String> phraseSourceWords = toRemove.stream()
                        .flatMap(u -> u.translation().sourceWords().stream())
                        .toList();
                String phraseText = toRemove.stream()
                        .map(u -> u.translation().processedTargetWord())
                        .collect(Collectors.joining(" "));
                String rawText = toRemove.stream()
                        .map(DocumentUnit::rawText)
                        .collect(Collectors.joining(" "));
                OwnerInfo ownerInfo = toRemove.getFirst().translation().ownerInfo();
                SelectedPhraseUnit selectedPhraseUnit = SelectedPhraseUnit.of(
                        unit.id(),
                        false,
                        UninitializedPhraseTranslation.of(
                                phraseSourceWords,
                                phraseText,
                                ownerInfo
                        ),
                        rawText
                );
                toRemove.clear();
                units.add(selectedPhraseUnit);
            }
            else {
                String phraseValue = selectedPhraseDetails.phraseText();
                if(phraseValue.contains(unit.translation().processedTargetWord())) {
                    buffer.add(unit);
                    units.add(unit);
                    String bufferValue = buffer.stream()
                            .map(u -> u.translation().processedTargetWord())
                            .collect(Collectors.joining(" "));
                    if(bufferValue.equals(phraseValue)) {
                        int bufferSize = bufferValue.split(" ").length;
                        int startId = units.size() - bufferSize;
                        int endId = units.size();
                        List<DocumentUnit> toRemove = units.subList(startId, endId);
                        List<String> phraseSourceWords = toRemove.stream()
                                .flatMap(u -> u.translation().sourceWords().stream())
                                .toList();
                        String phraseText = toRemove.stream()
                                .map(u -> u.translation().processedTargetWord())
                                .collect(Collectors.joining(" "));
                        String rawText = toRemove.stream()
                                .map(DocumentUnit::rawText)
                                .collect(Collectors.joining(" "));
                        OwnerInfo ownerInfo = toRemove.getFirst().translation().ownerInfo();
                        toRemove.clear();
                        units.add(PhraseUnit.of(
                                unit.id() - bufferSize + 1,
                                UninitializedPhraseTranslation.of(
                                        phraseSourceWords,
                                        phraseText,
                                        ownerInfo
                                ),
                                rawText,
                                bufferSize
                        ));
                        buffer.clear();
                    }
                }
                else {
                    units.add(unit);
                    buffer.clear();
                }
            }
        }
        else {
            units.add(unit);
        }
    }
    public static NewSelectedPhraseStore combine(NewSelectedPhraseStore l1, NewSelectedPhraseStore l2) {
        l1.units().addAll(l2.units());
        return l1;
    }
}
