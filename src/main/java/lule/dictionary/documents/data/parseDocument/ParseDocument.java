package lule.dictionary.documents.data.parseDocument;

public sealed interface ParseDocument permits ParseWithPhraseSelection, ParseWithWordSelection, ParseWithoutSelection {
    TranslationInfo translationInfo();
    OwnerInfo ownerInfo();
    String contentBlob();
}
