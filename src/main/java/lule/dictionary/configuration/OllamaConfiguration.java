package lule.dictionary.configuration;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.request.CustomModelRequest;
import io.github.ollama4j.types.OllamaModelType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
public class OllamaConfiguration {

    @Value("${ollama.api.baseUrl:http://localhost:11434}")
    private String baseUrl;

    @Bean
    public OllamaAPI ollamaAPI() throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        OllamaAPI ollamaAPI = new OllamaAPI(baseUrl);
        ollamaAPI.setVerbose(true);
//      ollamaAPI.pullModel(OllamaModelType.LLAMA3_1);
        ollamaAPI.createModel(CustomModelRequest.builder()
                .model("translator")
                .from(OllamaModelType.LLAMA3_1)
                .system("""
                        You are translator. Your task is to read user input and produce translation briefly.
                        If there are more translations separate them with a comma
                        Return as following json:
                        {
                            "translatedWord": "translatedWord"
                        }
                        """)
                .build());
        return ollamaAPI;
    }
}
