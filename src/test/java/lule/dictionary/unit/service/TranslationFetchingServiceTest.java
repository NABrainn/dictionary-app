package lule.dictionary.unit.service;

import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.translateAPI.google.GoogleTranslateService;
import lule.dictionary.service.translateAPI.lingvanex.LingvanexService;
import lule.dictionary.service.translationFetching.TranslationFetchingService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

public class TranslationFetchingServiceTest {

    private final TranslationFetchingService service;

    public TranslationFetchingServiceTest() {
        this.service = new TranslationFetchingService(
                new LingvanexService(
                        RestClient.builder()
                            .build(),
                        new LanguageHelper()
                ),
                new GoogleTranslateService(
                        RestClient.builder()
                                .build(),
                        new LanguageHelper()
                )
        );
    }

    @Test
    void shouldFetchTranslations() {
        List<String> result = service.fetchTranslationsAsync(Language.EN, Language.NO, "hopp til");
        System.out.println("fetch result: " + result);
    }
}
