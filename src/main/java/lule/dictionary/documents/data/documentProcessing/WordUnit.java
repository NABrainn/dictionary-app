package lule.dictionary.documents.data.documentProcessing;

public sealed interface WordUnit extends DocumentUnit permits NewWordUnit, PersistedWordUnit {
    boolean isPhrasePart();
}
