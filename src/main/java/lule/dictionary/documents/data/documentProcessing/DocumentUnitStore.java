package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

import java.util.List;
import java.util.stream.Collectors;

public record DocumentUnitStore(@NonNull List<DocumentUnit> phraseParts,
                                @NonNull List<DocumentUnit> documentUnits) {
    public String bufferValue() {
        return phraseParts.stream()
                .map(unit -> unit.translation().targetWord())
                .collect(Collectors.joining(" "));
    }

    public void addDocumentUnit(@NonNull DocumentUnit documentUnit) {
        documentUnits.add(documentUnit);
    }

    public void clearPhraseParts() {
        phraseParts().clear();
    }

    public static DocumentUnitStore of(List<DocumentUnit> phrasesInARow, List<DocumentUnit> documentUnits) {
        return new DocumentUnitStore(phrasesInARow, documentUnits);
    }

    public void addPhrasePart(@NonNull DocumentUnit documentUnit) {
        phraseParts().add(documentUnit);
    }

    public void wrapToPhrase(Translation translation) {
        int startId = documentUnits.size() - bufferValue().length();
        int endId = documentUnits().size();
        documentUnits.subList(startId, endId);
        addDocumentUnit(PhraseUnit.of(translation));
    }
}
