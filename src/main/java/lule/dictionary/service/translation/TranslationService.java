package lule.dictionary.service.translation;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.service.dto.result.ServiceResult;
import lule.dictionary.service.translation.dto.DeleteSourceWordRequest;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.translation.dto.attribute.TranslationPair;
import lule.dictionary.service.translation.dto.request.AddTranslationRequest;
import lule.dictionary.service.translation.dto.request.FindByTargetWordRequest;
import lule.dictionary.service.translation.dto.request.UpdateSourceWordsRequest;
import lule.dictionary.service.translation.dto.request.UpdateTranslationFamiliarityRequest;

import java.util.Map;

public interface TranslationService {
    ServiceResult<TranslationAttribute> createTranslation(AddTranslationRequest request);
    ServiceResult<TranslationAttribute> findByTargetWord(FindByTargetWordRequest request);
    ServiceResult<TranslationAttribute> updateFamiliarity(UpdateTranslationFamiliarityRequest request);
    ServiceResult<TranslationPair> updateSourceWords(UpdateSourceWordsRequest request);
    ServiceResult<TranslationPair> deleteSourceWord(DeleteSourceWordRequest request);
    ServiceResult<Integer> getWordsLearnedCount(String owner);
    ServiceResult<Map<String, Translation>> findTranslationsByImport(@NonNull Import imported, String owner);
}
