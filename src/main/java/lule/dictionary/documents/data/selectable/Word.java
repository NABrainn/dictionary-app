package lule.dictionary.documents.data.selectable;

import lombok.NonNull;

public record Word(@NonNull String value,
                   int id) implements Selectable {
    public static Word of(String value, int id) {
        return new Word(value, id);
    }
}
