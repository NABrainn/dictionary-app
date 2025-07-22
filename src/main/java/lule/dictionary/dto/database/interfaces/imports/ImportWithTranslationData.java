package lule.dictionary.dto.database.interfaces.imports;

public interface ImportWithTranslationData extends ImportWithId {
    int wordCount();
    int newWordCount();
    int translationCount();
}
