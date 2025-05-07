package lule.dictionary.integration;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.Import;
import lule.dictionary.entity.Translation;
import lule.dictionary.entity.UserProfile;
import lule.dictionary.entity.factory.ImportFactory;
import lule.dictionary.entity.factory.TranslationFactory;
import lule.dictionary.entity.factory.UserProfileFactory;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.repository.UserProfileRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void useCaseTest1() {
        UserProfile userProfile = UserProfileFactory.create("nabrain", "email@email.com", "password");
        Optional<UserProfile> addedUserProfile = userProfileRepository.addUserProfile(userProfile);
        addedUserProfile.ifPresent(value -> log.info("User Profile created: {}", value));

        Import anImport = ImportFactory.create("this is a title", "this is the content of the said import", "", Language.EN, Language.NO, "nabrain");
        OptionalInt addedImport = importRepository.addImport(anImport);
        addedImport.ifPresent(value -> log.info("Import created: {}, for User Profile: {}", value, anImport.owner()));

        Translation translation1 = TranslationFactory.create("one", "en", Language.EN, Language.NO, "nabrain", Familiarity.KNOWN);
        Translation translation2 = TranslationFactory.create("two", "to", Language.EN, Language.NO, "nabrain", Familiarity.UNKNOWN);
        Translation translation3 = TranslationFactory.create("three", "tre", Language.EN, Language.NO, "nabrain", Familiarity.RECOGNIZED);
        Translation translation4 = TranslationFactory.create("four", "fire", Language.EN, Language.NO, "nabrain", Familiarity.IGNORED);
        Translation translation5 = TranslationFactory.create("five", "fem", Language.EN, Language.NO, "nabrain", Familiarity.KNOWN);

        addedImport.ifPresent(value -> {
            translationRepository.addTranslation(translation1, value);
            translationRepository.addTranslation(translation2, value);
            translationRepository.addTranslation(translation3, value);
            translationRepository.addTranslation(translation4, value);
            translationRepository.addTranslation(translation5, value);
            assertEquals(5, translationRepository.findAllByImport(value).size());
        });
    }
}
