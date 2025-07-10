package lule.dictionary.service.translation;

import lule.dictionary.service.dto.result.ServiceResult;
import lule.dictionary.service.translation.dto.FindByTargetWordRequest;
import lule.dictionary.service.translation.dto.TranslationAttribute;
import lule.dictionary.service.translation.dto.UpdateTranslationFamiliarityRequest;
import lule.dictionary.service.translation.dto.request.AddTranslationRequest;

public interface TranslationService {
    ServiceResult<TranslationAttribute> createTranslation(AddTranslationRequest request);
    ServiceResult<TranslationAttribute> findByTargetWord(FindByTargetWordRequest request);
    ServiceResult<TranslationAttribute> updateFamiliarity(UpdateTranslationFamiliarityRequest request);
}
