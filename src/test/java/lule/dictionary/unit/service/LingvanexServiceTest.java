package lule.dictionary.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.translateAPI.lingvanex.LingvanexService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

public class LingvanexServiceTest {

    private final LingvanexService service;

    public LingvanexServiceTest() {
        RestClient restClient = RestClient.builder()
                .build();
        LanguageHelper languageHelper = new LanguageHelper();
        this.service = new LingvanexService(restClient, languageHelper);
    }

    @Test
    void shouldFetchTranslation() throws JsonProcessingException {
        List<String> result = service.translate(Language.EN, Language.NO, "hopp til");
        System.out.println(result);

    }
}
