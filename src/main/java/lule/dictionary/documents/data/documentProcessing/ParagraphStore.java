package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record ParagraphStore(@NonNull List<DocumentUnit> units,
                             @NonNull List<Paragraph> paragraphs,
                             @NonNull AtomicInteger counter) {
    public static ParagraphStore of(List<DocumentUnit> units,
                                    List<Paragraph> paragraphs,
                                    AtomicInteger counter) {
        return new ParagraphStore(units, paragraphs, counter);
    }

    public void extractParagraphs(@NonNull DocumentUnit unit) {
        this.units().add(unit);
        if(this.units().getLast().rawText().contains("\n")) {
            int firstId = 0;
            int lastId = this.units().size() - 1;
            List<DocumentUnit> paragraphUnits = new ArrayList<>(this.units().subList(firstId, lastId));
            this.paragraphs().add(Paragraph.of(this.counter().getAndIncrement(), paragraphUnits));
            this.units().subList(firstId, lastId).clear();
        }
    }
}
