package lule.dictionary.unit.service;

import lule.dictionary.language.service.Language;
import lule.dictionary.language.service.LanguageHelper;
import lule.dictionary.translationFetching.service.GoogleTranslateFetcher;
import lule.dictionary.translationFetching.service.LingvanexFetcher;
import lule.dictionary.translationFetching.service.TranslationFetchingExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

public class TranslationFetchingServiceTest {

    private final TranslationFetchingExecutor service;

    public TranslationFetchingServiceTest() {
        this.service = new TranslationFetchingExecutor(
                new LingvanexFetcher(
                        RestClient.builder()
                            .build(),
                        new LanguageHelper()
                ),
                new GoogleTranslateFetcher(
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
