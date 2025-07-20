package lule.dictionary.service.localization;

import lule.dictionary.service.language.Language;

import java.util.Map;

public interface LocalizationService {
    Map<String, String> navbarLocalization(Language language);
    Map<String, String> documentListLocalization(Language language);
    Map<String, String> authLocalization (Language language);
    Map<String, String> createImportLocalization(Language language);
    Map<String, String> translationFormLocalization(Language language);


}
