package lule.dictionary.service.translateAPI.libreTranslate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.translateAPI.libreTranslate.dto.TranslateResponse;
import lule.dictionary.service.translation.dto.request.FindByTargetWordRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibreTranslateService {

    private final RestClient libreTranslateClient;
    private final LanguageHelper languageHelper;

    @Value("${translation.libreTranslate.base.url}")
    private String baseUrl;

    public List<String> translate(FindByTargetWordRequest request) {
        try {
            TranslateResponse response = libreTranslateClient
                    .post()
                    .uri(baseUrl)
                    .body(Map.of(
                            "q", request.targetWord(),
                            "source", languageHelper.getCode(request.targetLanguage()),
                            "target", languageHelper.getCode(request.sourceLanguage()),
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
        } catch (HttpClientErrorException.BadRequest e) {
            log.warn("Invalid parameter provided: {}", e.getMessage());
            return List.of();
        }
    }
}
