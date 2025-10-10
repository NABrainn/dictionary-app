package lule.dictionary.documents.data.documentProcessing.collectorStore;

import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.DocumentUnit;
import lule.dictionary.documents.data.documentProcessing.Paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record ParagraphStore(@NonNull List<DocumentUnit> units,
                             @NonNull List<Paragraph> paragraphs,
                             @NonNull AtomicInteger counter) {
    public static ParagraphStore of(List<DocumentUnit> units, List<Paragraph> paragraphs, AtomicInteger counter) {
        return new ParagraphStore(units, paragraphs, counter);
    }

    public void accumulate(@NonNull DocumentUnit unit) {
        this.units().add(unit);
        int newlineCount = (int) Arrays.stream(unit.rawText().split(""))
                .filter(ch -> ch.equals("\n"))
                .count();
        if(unit.rawText().contains("\n") && newlineCount > 1) {
            List<DocumentUnit> paragraphUnits = new ArrayList<>(this.units());
            this.paragraphs().add(Paragraph.of(this.counter().getAndIncrement(), newlineCount, paragraphUnits));
            this.units().clear();
        }
    }
    public static ParagraphStore finish(@NonNull ParagraphStore store) {
        if (!store.units().isEmpty()) {
            List<DocumentUnit> paragraphUnits = new ArrayList<>(store.units());
            store.paragraphs().add(Paragraph.of(store.counter().getAndIncrement(), 0, paragraphUnits));
            store.units().clear();
        }
        return store;
    }
    public static ParagraphStore combine(@NonNull ParagraphStore left, @NonNull ParagraphStore right) {
        left.paragraphs().addAll(right.paragraphs());
        return left;
    }
}
