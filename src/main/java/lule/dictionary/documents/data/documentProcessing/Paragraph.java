package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;

import java.util.List;

public record Paragraph(int id,
                        int endlineSize,
                        @NonNull List<DocumentUnit> units) {
    public static Paragraph of(int id, int endlineSize, List<DocumentUnit> units) {
        return new Paragraph(id, endlineSize, units);
    }
    @Override
    public List<DocumentUnit> units() {
        return List.copyOf(units);
    }
}
