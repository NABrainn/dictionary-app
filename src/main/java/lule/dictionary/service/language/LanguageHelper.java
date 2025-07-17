package lule.dictionary.service.language;

import lule.dictionary.dto.application.LanguageData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LanguageHelper {

    private final String imageBasePath = "/images/flags";

    private final List<LanguageData> allLanguageData = List.of(
            LanguageData.of(Language.NO, "Norwegian", "nb", imageBasePath + "/norway.png"),
            LanguageData.of(Language.PL, "Polish", "pl", imageBasePath + "/poland.png"),
            LanguageData.of(Language.EN, "English", "en", imageBasePath + "/uk.png"),
            LanguageData.of(Language.IT, "Italian", "it", imageBasePath + "/italy.png")
    );

    public String getFullName(Language language) {
        return languageFullNames().get(language);
    }
    public String getAbbreviation(Language language) {
        return languageAbbreviations().get(language);
    }
    public String getImagePath(Language language) {
        return imagePaths().get(language);
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

    private Map<Language, String> imagePaths() {
        Map<Language, String> map = new HashMap<>();
        for (LanguageData languageData : allLanguageData) {
            map.put(languageData.language(), languageData.imgPath());
        }
        return map;
    }
}
