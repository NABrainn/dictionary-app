package lule.dictionary.service.ollama;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import lombok.RequiredArgsConstructor;
import lule.dictionary.service.language.Language;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

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
        String json = result.getResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<?, ?> translation = objectMapper.readValue(json, Map.class);
            if(translation.get("translatedWord") != null) return (String) translation.get("translatedWord");
            return "Something went wrong. Try again";
        } catch (JsonProcessingException e) {
            return "Something went wrong. Try again";
        }
    }
}
