package lule.dictionary.service.translation;

import lule.dictionary.controller.vocabularyController.dto.BaseFlashcardAttribute;
import lule.dictionary.controller.vocabularyController.dto.GetRandomTranslationsRequest;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.service.translation.dto.request.*;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;

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
