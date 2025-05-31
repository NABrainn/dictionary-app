package lule.dictionary.integration;

import lombok.extern.slf4j.Slf4j;

import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.dto.application.interfaces.translation.TranslationDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.factory.dto.ImportFactory;
import lule.dictionary.factory.dto.TranslationFactory;
import lule.dictionary.factory.dto.UserProfileFactory;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.repository.UserProfileRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;
import java.util.OptionalInt;

@Slf4j
@SpringBootTest
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/drop.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private TranslationRepository translationRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
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

    @Test
    void shouldCreateImportWithTranslations() {
        UserProfileCredentials credentials = UserProfileFactory.createCredentials(
                        "username",
                        "email@email.com",
                        "password"
        );
        UserProfileSettings settings = UserProfileFactory.createSettings(
                Language.EN,
                Language.NO
        );
        Optional<UserProfile> userProfile = userProfileRepository.addUserProfile(credentials, settings);

        userProfile.ifPresent(profile -> {
            ImportDetails importDetails = ImportFactory.createImportDetails(
                    "very profound title",
                    "plenty of content here",
                    "no url"
            );
            OptionalInt importId = importRepository.addImport(importDetails, profile);
            importId.ifPresent(id -> {
                TranslationDetails details = TranslationFactory.createTranslationDetails(
                        "monka",
                        "weed",
                        Familiarity.UNKNOWN
                );
                translationRepository.addTranslation(details, profile, id);
            });
        });
    }
}
