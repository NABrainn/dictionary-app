package lule.dictionary.documents.service;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.DocumentUnit;
import lule.dictionary.documents.data.documentProcessing.PhraseUnit;
import lule.dictionary.documents.data.documentProcessing.Phrases;
import lule.dictionary.documents.data.documentProcessing.WordUnit;
import lule.dictionary.translations.data.entity.Translation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Gatherer;

@Component
public class DocumentUnitGatherers {
    public Gatherer<WordUnit, ArrayList<DocumentUnit>, DocumentUnit> gatherDocumentUnits(@NonNull Phrases phrases) {
        return Gatherer.ofSequential(
                ArrayList::new,
                (buffer, wordUnit, downstream) -> {
                    if(downstream.isRejecting()) return false;
                    if (wordUnit.isPhrasePart()) {
                        buffer.add(wordUnit);
                        String normalizedPhrase = String.join(" ", buffer.stream()
                                        .map(unit -> unit.translation().processedTargetWord())
                                        .toList());
                        Optional<Translation> phraseOpt = phrases.containsPhrase(normalizedPhrase);
                        if (phraseOpt.isPresent()) {
                            int id = buffer.getFirst().id();
                            Translation phrase = phraseOpt.get();
                            String rawPhraseText = String.join(" ", buffer.stream()
                                            .map(DocumentUnit::rawText)
                                            .toList());
                            PhraseUnit phraseToAdd = PhraseUnit.of(
                                    id,
                                    phrase,
                                    rawPhraseText,
                                    buffer.size()
                            );
                            downstream.push(phraseToAdd);
                            buffer.clear();
                        }
                    }
                    else {
                        buffer.forEach(downstream::push);
                        downstream.push(wordUnit);
                        buffer.clear();
                    }
                    return true;
                },
                (buffer, downstream) -> buffer.forEach(downstream::push)
        );
    }
}
