package lule.dictionary.documents.data.documentProcessing;

public sealed interface WordUnit extends DocumentUnit permits NonTranslationWordUnit, TranslationWordUnit {
    boolean isPhrasePart();
}
