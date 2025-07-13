package lule.dictionary.service.language;

import lule.dictionary.dto.application.LanguageData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LanguageHelper {

    private final List<LanguageData> allLanguageData = List.of(
            LanguageData.of(Language.NO, "Norwegian", "nb"),
            LanguageData.of(Language.PL, "Polish", "pl"),
            LanguageData.of(Language.EN, "English", "en"),
            LanguageData.of(Language.IT, "Italian", "it")
    );

    public String getFullName(Language language) {
        return languageFullNames().get(language);
    }

    public String getAbbreviation(Language language) {
        return languageAbbreviations().get(language);
    }

    public List<LanguageData> getAllLanguageData() {
        return List.copyOf(allLanguageData);
    }

    private Map<Language, String> languageFullNames() {
        Map<Language, String> map = new HashMap<>();
        for (LanguageData languageData : allLanguageData) {
            map.put(languageData.language(), languageData.fullName());
        }
        return map;
    }

    private Map<Language, String> languageAbbreviations() {
        Map<Language, String> map = new HashMap<>();
        for (LanguageData languageData : allLanguageData) {
            map.put(languageData.language(), languageData.libreTranslateAbbreviation());
        }
        return map;
    }
}
