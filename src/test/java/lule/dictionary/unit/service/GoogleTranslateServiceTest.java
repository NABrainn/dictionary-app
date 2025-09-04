package lule.dictionary.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lule.dictionary.language.service.Language;
import lule.dictionary.language.service.LanguageHelper;
import lule.dictionary.translationFetching.service.GoogleTranslateFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

public class GoogleTranslateServiceTest {

    private final GoogleTranslateFetcher googleService;

    public GoogleTranslateServiceTest() {
        RestClient restClient = RestClient.builder()
                .build();
        LanguageHelper languageHelper = new LanguageHelper();
        this.googleService = new GoogleTranslateFetcher(restClient, languageHelper);
    }

    @Test
    void shouldFetchTranslation() throws JsonProcessingException {
        List<String> result = googleService.translate(Language.EN, Language.NO, "hopp til");
        System.out.println(result);

    }
}
