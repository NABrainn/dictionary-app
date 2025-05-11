package lule.dictionary.factory;


import lule.dictionary.dto.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

public class TranslationFactory {
    public static Translation create(int id, String sourceWord, String targetWord, Language sourceLanguage, Language targetLanguage, String owner, Familiarity familiarity) {
        return new Translation(id, sourceWord, targetWord, sourceLanguage, targetLanguage, owner, familiarity);
    }
}
