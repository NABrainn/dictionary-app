package lule.dictionary.localization.service;

import lule.dictionary.language.service.Language;

import java.util.Map;

public interface LocalizationService {
    Map<String, String> navbarLocalization(Language language);
    Map<String, String> documentListLocalization(Language language);
    Map<String, String> authLocalization (Language language);
    Map<String, String> documentFormLocalization(Language language);
    Map<String, String> translationFormLocalization(Language language);
    Map<String, String> flashcardConfigLocalization(Language language);
    Map<String, String> settingsLocalization(Language language);
}
