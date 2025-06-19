package lule.dictionary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient libreTranslateClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:5000")
                .build();
    }
}
