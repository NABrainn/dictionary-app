package lule.dictionary.language.service;

import lule.dictionary.controllerAdvice.data.navbar.LanguageOption;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    public List<LanguageOption> languageOptions() {
        String imageBasePath = "/images/flags";
        return List.of(
            LanguageOption.of(Language.NO, imageBasePath + "/norway.png"),
            LanguageOption.of(Language.EN, imageBasePath + "/uk.png"),
            LanguageOption.of(Language.IT, imageBasePath + "/italy.png"),
            LanguageOption.of(Language.PL, imageBasePath + "/poland.png")
        );
    }
}
