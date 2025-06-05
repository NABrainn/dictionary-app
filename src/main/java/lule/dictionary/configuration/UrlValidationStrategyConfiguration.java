package lule.dictionary.configuration;

import lule.dictionary.validation.implementation.UrlValidationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UrlValidationStrategyConfiguration {

    @Bean
    public UrlValidationStrategy urlValidationStrategy() {
        return new UrlValidationStrategy();
    }
}
