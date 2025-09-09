package lule.dictionary.documents.data.selectable;

public sealed interface Selectable permits Word, Phrase {
    int id();
}
