package lule.dictionary.documents.data.parseDocument;

import lule.dictionary.userProfiles.data.OwnerInfo;

public sealed interface ParseDocument permits ParseWithPhraseSelection, ParseWithWordSelection, ParseWithoutSelection {
    TranslationInfo translationInfo();
    OwnerInfo ownerInfo();
    String contentBlob();
}
