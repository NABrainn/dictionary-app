package lule.dictionary.documents.data.documentProcessing.collectorStore;

import lule.dictionary.documents.data.documentProcessing.DocumentUnit;
import lule.dictionary.documents.data.documentProcessing.PhraseUnit;
import lule.dictionary.documents.data.documentProcessing.SelectedPhraseUnit;
import lule.dictionary.documents.data.request.loadDocument.PersistedSelectedPhraseDetails;
import lule.dictionary.documents.data.request.loadDocument.SelectedPhraseDetails;
import lule.dictionary.documents.data.request.loadDocument.UninitializedSelectedPhraseDetails;

import java.util.ArrayList;

public record ExistingSelectedPhraseStore(ArrayList<DocumentUnit> units) {
    public static ExistingSelectedPhraseStore of(ArrayList<DocumentUnit> units) {
        return new ExistingSelectedPhraseStore(units);
    }
    public void accumulate(DocumentUnit unit, SelectedPhraseDetails selectedPhraseDetails) {
        if(unit instanceof PhraseUnit phraseUnit) {
            if(phraseUnit.id() == selectedPhraseDetails.startId()) {
                SelectedPhraseUnit selectedPhraseUnit = SelectedPhraseUnit.of(
                        unit.id(),
                        switch (selectedPhraseDetails) {
                            case PersistedSelectedPhraseDetails ignored -> true;
                            case UninitializedSelectedPhraseDetails ignored -> false;
                        },
                        unit.translation(),
                        unit.rawText()
                );
                units.add(selectedPhraseUnit);
            }
            else {
                units.add(unit);
            }
        }
        else {
            units.add(unit);
        }
    }
    public static ExistingSelectedPhraseStore combine(ExistingSelectedPhraseStore l1, ExistingSelectedPhraseStore l2) {
        l1.units().addAll(l2.units());
        return l1;
    }
}
