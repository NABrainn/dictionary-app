package lule.dictionary.service.translation;

import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.base.Import;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.translation.dto.request.DeleteSourceWordRequest;
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
    ServiceResult<Integer> getWordsLearnedCount(String owner, Language targetLanguage);
    ServiceResult<Map<String, Translation>> findTranslationsByImport(@NonNull Import imported, String owner);
}
