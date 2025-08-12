package lule.dictionary.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.translateAPI.google.GoogleTranslateService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

public class GoogleTranslateServiceTest {

    private final GoogleTranslateService googleService;

    public GoogleTranslateServiceTest() {
        RestClient restClient = RestClient.builder()
                .build();
        LanguageHelper languageHelper = new LanguageHelper();
        this.googleService = new GoogleTranslateService(restClient, languageHelper);
    }

    @Test
    void shouldFetchTranslation() throws JsonProcessingException {
        List<String> result = googleService.translate(Language.EN, Language.NO, "hopp til");
        System.out.println(result);

    }
}
