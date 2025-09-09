package lule.dictionary.documents.data;

import lombok.NonNull;
import lule.dictionary.documents.data.selectable.Selectable;

import java.util.List;

public record Paragraph(@NonNull List<Selectable> selectables,
                        int id) {

    public static Paragraph of(List<Selectable> selectables, int id) {
        return new Paragraph(selectables, id);
    }

    @Override
    public List<Selectable> selectables() {
        return List.copyOf(selectables);
    }
}
