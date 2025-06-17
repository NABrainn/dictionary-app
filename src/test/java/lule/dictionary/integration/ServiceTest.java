package lule.dictionary.integration;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.service.imports.importService.ImportService;
import lule.dictionary.service.userProfile.UserProfileService;
import lule.dictionary.service.imports.importPageService.ImportPageService;
import lule.dictionary.service.translation.TranslationService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@SpringBootTest
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/drop.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ServiceTest {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ImportService importService;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private ImportPageService dictionaryService;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.login", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }


}
