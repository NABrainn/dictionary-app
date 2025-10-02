package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;

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
}
