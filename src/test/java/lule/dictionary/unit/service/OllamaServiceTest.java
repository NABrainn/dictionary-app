package lule.dictionary.unit.service;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import lule.dictionary.enumeration.Language;
import lule.dictionary.service.ollama.OllamaService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class OllamaServiceTest {

    private final OllamaService ollamaService;

    public OllamaServiceTest() {
        this.ollamaService = new OllamaService(new OllamaAPI("http://localhost:11434"));
    }

    @Test
    void shouldTranslate() throws OllamaBaseException, IOException, InterruptedException {
        String trans1 = ollamaService.translate(Language.NO, Language.EN, "ordet");
        String trans2 = ollamaService.translate(Language.EN, Language.NO, "the word");
        String trans3 = ollamaService.translate(Language.EN, Language.NO, "how goes it");
        List<String> results = List.of(trans1, trans2, trans3);
        for(var result : results) {
            System.out.println(result);
        }
    }
}
