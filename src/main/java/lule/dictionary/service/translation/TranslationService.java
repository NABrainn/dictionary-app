package lule.dictionary.service.translation;

import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.service.dto.result.ServiceResult;
import lule.dictionary.service.translation.dto.DeleteSourceWordRequest;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.translation.dto.attribute.TranslationPair;
import lule.dictionary.service.translation.dto.request.AddTranslationRequest;
import lule.dictionary.service.translation.dto.request.FindByTargetWordRequest;
import lule.dictionary.service.translation.dto.request.UpdateSourceWordsRequest;
import lule.dictionary.service.translation.dto.request.UpdateTranslationFamiliarityRequest;

import java.util.List;

public interface TranslationService {
    ServiceResult<TranslationAttribute> createTranslation(AddTranslationRequest request);
    ServiceResult<TranslationAttribute> findByTargetWord(FindByTargetWordRequest request);
    ServiceResult<TranslationAttribute> updateFamiliarity(UpdateTranslationFamiliarityRequest request);
    ServiceResult<TranslationPair> updateSourceWords(UpdateSourceWordsRequest request);
    ServiceResult<TranslationPair> deleteSourceWord(DeleteSourceWordRequest request);
    List<Translation> findByTargetWords(List<String> targetWords, String owner);
    int getWordsLearnedCount(String owner);
}
