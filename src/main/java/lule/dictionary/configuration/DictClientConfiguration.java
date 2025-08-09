package lule.dictionary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DictClientConfiguration {
    @Bean
    public RestClient dictClient() {
        return RestClient.builder()
                .baseUrl("https://www.dict.cc")
                .build();
    }
}
