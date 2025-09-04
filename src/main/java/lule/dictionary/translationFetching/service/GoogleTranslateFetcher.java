package lule.dictionary.translationFetching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.language.service.LanguageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleTranslateFetcher implements TranslationFetcher {

    private final RestClient googleTranslateClient;
    private final LanguageHelper languageHelper;

    @Override
    public List<String> translate(Language sourceLanguage, Language targetLanguage, String targetWord) throws JsonProcessingException {
        String sourceLanguageCode = languageHelper.getCode(sourceLanguage);
        String targetLanguageCode = languageHelper.getCode(targetLanguage);
        String response =  googleTranslateClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("translate.googleapis.com")
                        .path("/translate_a")
                        .path("/single")
                        .queryParam("client", "gtx")
                        .queryParam("dt", "t")
                        .queryParam("sl", targetLanguageCode)
                        .queryParam("tl", sourceLanguageCode)
                        .queryParam("q", targetWord)
                        .build())
                .retrieve()
                .body(String.class);
        return extractTranslation(targetWord, response);
    }

    private List<String> extractTranslation(String targetWord, String response) throws JsonProcessingException {
        if(response != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode json = mapper.readTree(response);
                String trimmedJson = leftRightTrim(targetWord, json.toString());
                return List.of(trimmedJson);
            } catch (JsonProcessingException e) {
                return List.of();
            }

        }
        return List.of();
    }

    private String leftRightTrim(String searchTarget, String text) {
        int indexOfSearchTarget = text.indexOf(searchTarget) ;
        if(indexOfSearchTarget == -1)
            return "";
        return text.substring(4, indexOfSearchTarget - 3);
    }
}
