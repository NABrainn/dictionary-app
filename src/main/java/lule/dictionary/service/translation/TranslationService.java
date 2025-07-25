package lule.dictionary.service.translation;

import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.translation.dto.request.*;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;

import java.util.Map;

public interface TranslationService {
    ServiceResult<TranslationAttribute> createTranslation(AddTranslationRequest request);
    ServiceResult<TranslationAttribute> findByTargetWord(FindByTargetWordRequest request);
    ServiceResult<TranslationAttribute> updateFamiliarity(UpdateTranslationFamiliarityRequest request);
    ServiceResult<TranslationAttribute> updateSourceWords(UpdateSourceWordsRequest request);
    ServiceResult<TranslationAttribute> deleteSourceWord(DeleteSourceWordRequest request);
    ServiceResult<Integer> getWordsLearnedCount(GetWordsLearnedCountRequest request);
    ServiceResult<Map<String, Translation>> findTranslationsByImport(FindTranslationsByImportRequest request);
}
