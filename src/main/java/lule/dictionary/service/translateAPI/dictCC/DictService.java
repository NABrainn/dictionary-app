package lule.dictionary.service.translateAPI.dictCC;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DictService {

    private final RestClient dictClient;
    private final LanguageHelper languageHelper;

    public String translate(Language sourceLanguage, Language targetLanguage, String targetWord) {
        String sourceLanguageCode = languageHelper.getCode(sourceLanguage);
        String targetLanguageCode = languageHelper.getCode(targetLanguage);
        String response =  dictClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(targetLanguageCode + sourceLanguageCode + ".dict.cc")
                        .path("/")
                        .queryParam("s", targetWord)
                        .build())
                .retrieve()
                .body(String.class);
        return extractTranslation(response);
    }

    private String extractTranslation(String response) {
        if(response != null) {
            String leftTrimmedString = leftTrim("var c2Arr = new Array(", response);
            String leftRightTrimmedString = rightTrim(";", leftTrimmedString);
            List<String> results = Arrays.stream(leftRightTrimmedString.split(","))
                    .map(word -> word.substring(1, word.length() - 1))
                    .toList();
            return results.size() >= 2 ? results.get(1) : "";
        }
        return "";
    }

    private String leftTrim(String searchTarget, String text) {
        int indexOfSearchTarget = text.indexOf(searchTarget) + 22;
        if(indexOfSearchTarget == -1)
            return "";
        return text.substring(indexOfSearchTarget);
    }

    private String rightTrim(String searchTarget, String text) {
        int indexOfSearchTarget = text.indexOf(searchTarget) - 1;
        if(indexOfSearchTarget == -1)
            return "";
        return text.substring(0, text.indexOf(searchTarget) - 1);
    }
}
