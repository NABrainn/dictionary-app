package lule.dictionary.translations.data.entity;

import lule.dictionary.userProfiles.data.OwnerInfo;
import lule.dictionary.translations.data.Familiarity;

import java.util.List;

public sealed interface Translation permits Phrase, UninitializedTranslation, Word {
    List<String> sourceWords();
    String processedTargetWord();
    Familiarity familiarity();
    OwnerInfo ownerInfo();
    Translation withSourceWords(List<String> sourceWords);
}
