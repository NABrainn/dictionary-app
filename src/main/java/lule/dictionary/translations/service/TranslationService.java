package lule.dictionary.translations.service;

import lule.dictionary.translations.controller.vocabulary.dto.BaseFlashcardAttribute;
import lule.dictionary.translations.controller.vocabulary.dto.GetRandomTranslationsRequest;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.request.*;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.translations.data.attribute.TranslationAttribute;

import java.util.List;
import java.util.Map;

public interface TranslationService {
    TranslationAttribute createTranslation(AddTranslationRequest request);
    TranslationAttribute findByTargetWord(FindByTargetWordRequest request);
    TranslationAttribute updateFamiliarity(UpdateTranslationFamiliarityRequest request);
    TranslationAttribute updateSourceWords(UpdateSourceWordsRequest request);
    TranslationAttribute deleteSourceWord(DeleteSourceWordRequest request);
    int getWordsLearnedCount(UserProfile principal);
    Map<String, Translation> findTranslationsByImport(FindTranslationsByImportRequest request);
    List<Translation> extractPhrases(String content, String owner);
    List<String> translate(TranslateRequest request);
    BaseFlashcardAttribute getRandomTranslations(GetRandomTranslationsRequest request);
}
