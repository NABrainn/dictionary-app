package lule.dictionary.service.libreTranslate;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.libreTranslate.dto.TranslateResponse;
import lule.dictionary.service.translation.dto.request.FindByTargetWordRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LibreTranslateService {

    private final RestClient libreTranslateClient;
    private final LanguageHelper languageHelper;

    @Value("${translation.libreTranslate.base.url}")
    private String baseUrl;

    public List<String> translate(FindByTargetWordRequest request) {
        TranslateResponse response = libreTranslateClient
                .post()
                .uri(baseUrl)
                .body(Map.of(
                        "q", request.targetWord(),
                        "source", languageHelper.getAbbreviation(request.targetLanguage()),
                        "target", languageHelper.getAbbreviation(request.sourceLanguage()),
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
