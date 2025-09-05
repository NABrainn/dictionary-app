package lule.dictionary.documents.data.entity;

public interface DocumentWithTranslationData extends DocumentWithId {
    int wordCount();
    int newWordCount();
    int translationCount();
}
