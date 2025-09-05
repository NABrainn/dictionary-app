package lule.dictionary.documents.data.selectable;

public record Word(String value) implements Selectable {
    public static Word of(String value) {
        return new Word(value);
    }
}
