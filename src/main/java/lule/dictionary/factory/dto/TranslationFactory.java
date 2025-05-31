package lule.dictionary.factory.dto;


import lule.dictionary.dto.application.implementation.translation.DictionaryTranslation;
import lule.dictionary.dto.application.implementation.translation.DictionaryTranslationDetails;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.dto.application.interfaces.translation.TranslationDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.enumeration.Familiarity;

public class TranslationFactory {
    public static Translation createTranslation(
            TranslationDetails translationDetails,
            UserProfile userProfile) {
        return new DictionaryTranslation(translationDetails, userProfile);
    }
    public static TranslationDetails createTranslationDetails(
             String sourceWord,
             String targetWord,
             Familiarity familiarity) {
        return new DictionaryTranslationDetails(sourceWord, targetWord, familiarity);
    }
}
