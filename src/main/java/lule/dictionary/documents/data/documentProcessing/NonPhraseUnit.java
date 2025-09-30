package lule.dictionary.documents.data.documentProcessing;

public sealed interface NonPhraseUnit permits NonTranslationWordUnit, TranslationWordUnit {
    boolean isPhrasePart();
}
