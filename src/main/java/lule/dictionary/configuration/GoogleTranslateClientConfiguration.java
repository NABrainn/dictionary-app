package lule.dictionary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GoogleTranslateClientConfiguration {
    @Bean
    public RestClient googleTranslateClient() {
        return RestClient.builder()
                .build();
    }
}
