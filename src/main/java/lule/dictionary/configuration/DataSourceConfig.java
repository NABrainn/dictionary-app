package lule.dictionary.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    JdbcConnectionDetails primaryJdbcConnectionDetails(
            @Qualifier("jdbcConnectionDetailsForDictappPostgres1") JdbcConnectionDetails connectionDetails) {
        return connectionDetails;
    }
}
