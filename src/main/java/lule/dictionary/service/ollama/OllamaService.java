package lule.dictionary.service.ollama;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import lombok.RequiredArgsConstructor;
import lule.dictionary.enumeration.Language;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OllamaService {

    private final OllamaAPI ollamaAPI;

    public String translate(Language targetLanguage, Language sourceLanguage, String input) throws OllamaBaseException, IOException, InterruptedException {
        OllamaResult result = ollamaAPI.generate(
                "translator",
                String.format("""
                        translate from: %s
                        translate to: %s
                        translate word: %s
                        """, targetLanguage.name(), sourceLanguage.name(), input),
                null
        );
        return result.getResponse();
    }
}
