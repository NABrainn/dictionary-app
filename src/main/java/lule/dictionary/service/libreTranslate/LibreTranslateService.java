package lule.dictionary.service.libreTranslate;

import lombok.RequiredArgsConstructor;
import lule.dictionary.enumeration.Language;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LibreTranslateService {

    private final RestClient libreTranslateClient;

    public TranslateResponse translate(String targetWord, Language sourceLanguage, Language targetLanguage) {
        return libreTranslateClient
                .post()
                .uri("/translate")
                .body(Map.of(
                        "q", targetWord,
                        "source", languageMap().get(targetLanguage.name()),
                        "target", languageMap().get(sourceLanguage.name()),
                        "format", "text",
                        "alternatives", 3,
                        "api_key", ""
                ))
                .retrieve()
                .body(TranslateResponse.class);
    }

    public Map<String, String> languageMap() {
        return Map.of(
          Language.PL.name(), "pl",
          Language.EN.name(), "en",
          Language.NO.name(), "nb"
        );
    }
}
