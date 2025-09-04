package lule.dictionary.translations.service;

import lule.dictionary.vocabulary.controller.dto.BaseFlashcardAttribute;
import lule.dictionary.vocabulary.controller.dto.GetRandomTranslationsRequest;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.userProfiles.data.CustomUserDetails;
import lule.dictionary.translations.service.dto.request.*;
import lule.dictionary.translations.service.dto.attribute.TranslationAttribute;

import java.util.List;
import java.util.Map;

public interface TranslationService {
    TranslationAttribute createTranslation(AddTranslationRequest request);
    TranslationAttribute findByTargetWord(FindByTargetWordRequest request);
    TranslationAttribute updateFamiliarity(UpdateTranslationFamiliarityRequest request);
    TranslationAttribute updateSourceWords(UpdateSourceWordsRequest request);
    TranslationAttribute deleteSourceWord(DeleteSourceWordRequest request);
    int getWordsLearnedCount(CustomUserDetails principal);
    Map<String, Translation> findTranslationsByImport(FindTranslationsByImportRequest request);
    List<Translation> extractPhrases(String content, String owner);
    List<String> translate(TranslateRequest request);
    BaseFlashcardAttribute getRandomTranslations(GetRandomTranslationsRequest request);
}
