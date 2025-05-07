package lule.dictionary.entity.factory;


import lule.dictionary.entity.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

public class TranslationFactory {
    public static Translation create(String sourceWord, String targetWord, Language sourceLanguage, Language targetLanguage, String owner, Familiarity familiarity) {
        return new Translation(sourceWord, targetWord, sourceLanguage, targetLanguage, owner, familiarity);
    }
}
