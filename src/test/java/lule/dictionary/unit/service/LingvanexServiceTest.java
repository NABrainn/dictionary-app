package lule.dictionary.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lule.dictionary.language.service.Language;
import lule.dictionary.language.service.LanguageHelper;
import lule.dictionary.translationFetching.service.LingvanexFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

public class LingvanexServiceTest {

    private final LingvanexFetcher service;

    public LingvanexServiceTest() {
        RestClient restClient = RestClient.builder()
                .build();
        LanguageHelper languageHelper = new LanguageHelper();
        this.service = new LingvanexFetcher(restClient, languageHelper);
    }

    @Test
    void shouldFetchTranslation() throws JsonProcessingException {
        List<String> result = service.translate(Language.EN, Language.NO, "hopp til");
        System.out.println(result);

    }
}
