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
            LanguageData.builder()
                    .language(Language.NO)
                    .fullName("Norwegian")
                    .languageCode("no")
                    .imgPath(imageBasePath + "/norway.png")
                    .build(),
            LanguageData.builder()
                    .language(Language.EN)
                    .fullName("English")
                    .languageCode("en")
                    .imgPath(imageBasePath + "/uk.png")
                    .build(),
            LanguageData.builder()
                    .language(Language.IT)
                    .fullName("Italian")
                    .languageCode("it")
                    .imgPath(imageBasePath + "/italy.png")
                    .build(),
            LanguageData.builder()
                    .language(Language.PL)
                    .fullName("Polish")
                    .languageCode("pl")
                    .imgPath(imageBasePath + "/poland.png")
                    .build()
    );

    public String getFullName(Language language) {
        return languageFullNames().get(language);
    }
    public String getCode(Language language) {
        return languageCodes().get(language);
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

    private Map<Language, String> languageCodes() {
        Map<Language, String> map = new HashMap<>();
        for (LanguageData languageData : allLanguageData) {
            map.put(languageData.language(), languageData.languageCode());
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
