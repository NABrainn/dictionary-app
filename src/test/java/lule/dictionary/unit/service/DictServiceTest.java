package lule.dictionary.unit.service;

import lule.dictionary.service.language.Language;
import lule.dictionary.service.language.LanguageHelper;
import lule.dictionary.service.translateAPI.dictCC.DictService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class DictServiceTest {

    private final DictService dictService;

    public DictServiceTest() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://www.dict.cc")
                .build();
        LanguageHelper languageHelper = new LanguageHelper();
        this.dictService = new DictService(restClient, languageHelper);
    }
}
