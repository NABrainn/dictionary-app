package lule.dictionary.integration;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.dto.application.interfaces.translation.TranslationDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.ResourceNotFoundException;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.factory.dto.ImportFactory;
import lule.dictionary.factory.dto.TranslationFactory;
import lule.dictionary.factory.dto.UserProfileFactory;
import lule.dictionary.service.application.ImportData;
import lule.dictionary.service.application.entity.ImportService;
import lule.dictionary.service.application.entity.TranslationService;
import lule.dictionary.service.application.entity.UserProfileService;
import lule.dictionary.service.application.integration.DictionaryService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.util.Optional;

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
    private DictionaryService dictionaryService;

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
    void testScenario1() throws IOException {
        UserProfile userProfile = testLoadUserProfile();
        UserProfileSettings userProfileSettings = userProfile.userProfileSettings();
        String username = userProfile.userProfileCredentials().username();
        int importId = dictionaryService.addImport("Am importing a content thing", "yr.no", userProfileSettings, username);
        ImportData importData = dictionaryService.loadImport(importId);
        log.info("""
                {}
                {}
                """, importData.title(), importData.content());
    }

    private UserProfile testLoadUserProfile() {
        UserProfileCredentials credentials = UserProfileFactory.createCredentials(
                "username",
                "email@email.com",
                "password"
        );
        UserProfileSettings settings = UserProfileFactory.createSettings(
                Language.EN,
                Language.NO
        );
        return userProfileService.addUserProfile(UserProfileFactory.createUserProfile(credentials, settings));
    }

    private int testAddImport(UserProfileSettings userProfileSettings, String owner) {
        int addedImport = importService.addImport(ImportFactory.createImport(
                ImportFactory.createImportDetails(
                        "title",
                        "content",
                        "url"
                ),
                userProfileSettings,
                owner));
        return addedImport;
    }

    private void testAddMultipleTranslations(UserProfileSettings userProfileSettings, String owner, int importId) {
        for(int i = 1; i <= 5; i++) {
            TranslationDetails translationDetails = TranslationFactory.createTranslationDetails(
                    "one".repeat(i),
                    "en".repeat(i),
                    Familiarity.UNKNOWN
            );
            translationService.add(
                    TranslationFactory.createTranslation(
                            translationDetails,
                            userProfileSettings,
                            owner
                    ),
                    importId
            );
        }
    }
}
