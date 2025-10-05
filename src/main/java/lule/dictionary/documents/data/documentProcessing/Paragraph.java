package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;

import java.util.List;

public record Paragraph(int id,
                        @NonNull List<DocumentUnit> units) {
    public static Paragraph of(int id, List<DocumentUnit> units) {
        return new Paragraph(id, units);
    }
    @Override
    public List<DocumentUnit> units() {
        return List.copyOf(units);
    }
}
