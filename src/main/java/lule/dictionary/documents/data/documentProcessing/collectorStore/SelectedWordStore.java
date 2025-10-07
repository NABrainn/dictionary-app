package lule.dictionary.documents.data.documentProcessing.collectorStore;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.DocumentUnit;
import lule.dictionary.documents.data.documentProcessing.SelectedWordUnit;
import lule.dictionary.documents.data.documentProcessing.WordUnit;
import lule.dictionary.documents.data.request.loadDocument.PersistedSelectedWordDetails;
import lule.dictionary.documents.data.request.loadDocument.SelectedWordDetails;
import lule.dictionary.documents.data.request.loadDocument.UninitializedSelectedWordDetails;

import java.util.ArrayList;

public record SelectedWordStore(@NonNull ArrayList<DocumentUnit> units) {
    public static SelectedWordStore of(ArrayList<DocumentUnit> units) {
        return new SelectedWordStore(units);
    }

    public void accumulate(DocumentUnit unit, @NonNull SelectedWordDetails selectedWordDetails) {
        if(unit instanceof WordUnit && unit.id() == selectedWordDetails.startId()) {
            SelectedWordUnit selectedWordUnit = SelectedWordUnit.of(
                    unit.id(),
                    switch (selectedWordDetails) {
                        case PersistedSelectedWordDetails ignored -> true;
                        case UninitializedSelectedWordDetails ignored -> false;
                    },
                    unit.translation(),
                    unit.rawText()
            );
            units.add(selectedWordUnit);
        }
        else {
            units.add(unit);
        }
    }
    public static SelectedWordStore combine(SelectedWordStore l1, SelectedWordStore l2) {
        l1.units().addAll(l2.units());
        return l1;
    }
}
