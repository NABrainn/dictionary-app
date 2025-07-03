package lule.dictionary.service.libreTranslate;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.libreTranslate.dto.TranslateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static lule.dictionary.service.language.LanguageHelper.languageMapAbbreviations;
@Service
@RequiredArgsConstructor
public class LibreTranslateService {

    private final RestClient libreTranslateClient;
    @Value("${translation.libreTranslate.base.url}")
    private String baseUrl;

    public List<String> translate(String targetWord, Language sourceLanguage, Language targetLanguage) {
        TranslateResponse response = libreTranslateClient
                .post()
                .uri(baseUrl)
                .body(Map.of(
                        "q", targetWord,
                        "source", languageMapAbbreviations.get(targetLanguage),
                        "target", languageMapAbbreviations.get(sourceLanguage),
                        "format", "text",
                        "alternatives", 3,
                        "api_key", ""
                ))
                .retrieve()
                .body(TranslateResponse.class);
        if(response != null) {
            return List.of(response.translatedText());
        }
        return List.of();
    }
}
