package lule.dictionary.translationFetching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.language.service.LanguageHelper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LingvanexFetcher implements TranslationFetcher {

    private final RestClient lingvanexClient;
    private final LanguageHelper languageHelper;

    @Override
    public List<String> translate(Language sourceLanguage, Language targetLanguage, String targetWord) throws JsonProcessingException {
        String sourceLanguageCode = languageHelper.getCode(sourceLanguage);
        String targetLanguageCode = languageHelper.getCode(targetLanguage);
        var formData = assembleFormData(targetWord, targetLanguageCode, sourceLanguageCode);
        String response = lingvanexClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("lingvanex.com")
                        .path("/translation/translate")
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(String.class);
        if(response != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            String result = json.get("result").toString();
            return List.of(extractSourceWord(result));
        }
        return List.of();
    }

    private String extractSourceWord(String result) {
        return result.substring(1, result.length() - 1).replace("-", " ");
    }

    private LinkedMultiValueMap<String, String> assembleFormData(String targetWord, String targetLanguageCode, String sourceLanguageCode) {
        var formData = new LinkedMultiValueMap<String, String>();
        formData.add("from_lang", getFromLangCode(targetLanguageCode));
        formData.add("to", getToLangCode(sourceLanguageCode));
        formData.add("text", targetWord.replace(" ", "-"));
        formData.add("platform", "dp");
        return formData;
    }

    private String getToLangCode(String sourceLanguageCode) {
        return switch (sourceLanguageCode) {
            case "en" -> "en_US";
            default -> throw new IllegalStateException("Unexpected value: " + sourceLanguageCode);
        };
    }

    private String getFromLangCode(String targetLanguageCode) {
        return switch (targetLanguageCode) {
            case "no" -> "no_NO";
            default -> throw new IllegalStateException("Unexpected value: " + targetLanguageCode);
        };
    }
}
