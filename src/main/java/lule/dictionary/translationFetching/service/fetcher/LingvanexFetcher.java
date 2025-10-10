package lule.dictionary.translationFetching.service.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.language.service.LanguageService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LingvanexFetcher implements TranslationFetcher {

    private final LanguageService languageService;

    @Override
    public List<String> translate(Language sourceLanguage, Language targetLanguage, String targetWord) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String sourceLanguageCode = languageService.getCode(sourceLanguage);
        String targetLanguageCode = languageService.getCode(targetLanguage);
        return Optional.ofNullable(RestClient.builder()
                        .build()
                        .post()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("lingvanex.com")
                                .path("/translation/translate")
                                .build())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(Map.of(
                                "from_lang", switch (targetLanguageCode) {
                                    case "no" -> "no_NO";
                                    case "en" -> "en_US";
                                    case "it" -> "it_IT";
                                    case "pl" -> "pl_PL";
                                    default -> "";
                                },
                                "to", switch (sourceLanguageCode) {
                                    case "no" -> "no_NO";
                                    case "en" -> "en_US";
                                    case "it" -> "it_IT";
                                    case "pl" -> "pl_PL";
                                    default -> "";
                                },
                                "text", targetWord.replace(" ", "-"),
                                "platform", "dp"
                        ))
                        .retrieve()
                        .body(String.class))
                .map(jsonBlob -> {
                    try {
                        return mapper.readTree(jsonBlob);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(jsonNode -> jsonNode.get("result"))
                .map(Objects::toString)
                .map(json -> json.substring(1, json.length() - 1).replace("-", " "))
                .map(List::of)
                .orElse(List.of());
    }
}
