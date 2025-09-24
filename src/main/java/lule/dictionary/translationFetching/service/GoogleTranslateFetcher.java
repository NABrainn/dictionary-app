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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleTranslateFetcher implements TranslationFetcher {

    private final LanguageHelper languageHelper;

    @Override
    public List<String> translate(Language sourceLanguage, Language targetLanguage, String targetWord) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String sourceLanguageCode = languageHelper.getCode(sourceLanguage);
        String targetLanguageCode = languageHelper.getCode(targetLanguage);
        String response =  RestClient.builder()
                .build()
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
        return Optional.ofNullable(response)
                .map(responseBlob -> {
                    try {
                        return mapper.readTree(responseBlob);
                    } catch (JsonProcessingException e) {
                        return List.of();
                    }
                })
                .map(Object::toString)
                .map(json -> !json.contains(targetWord) ?
                        "" :
                        json.substring(4, json.indexOf(targetWord) - 3))
                .map(List::of)
                .orElse(List.of());
    }
}
