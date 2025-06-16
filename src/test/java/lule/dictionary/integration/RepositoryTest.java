//package lule.dictionary.integration;
//
//import lombok.extern.slf4j.Slf4j;
//
//import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
//import lule.dictionary.dto.application.interfaces.translation.Translation;
//import lule.dictionary.dto.application.interfaces.translation.TranslationDetails;
//import lule.dictionary.dto.application.interfaces.userProfile.base.UserProfile;
//import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
//import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
//import lule.dictionary.enumeration.Familiarity;
//import lule.dictionary.enumeration.Language;
//import lule.dictionary.factory.dto.ImportFactory;
//import lule.dictionary.factory.dto.TranslationFactory;
//import lule.dictionary.factory.dto.UserProfileFactory;
//import lule.dictionary.repository.ImportRepository;
//import lule.dictionary.repository.TranslationRepository;
//import lule.dictionary.repository.UserProfileRepository;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.jdbc.Sql;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//import java.util.Optional;
//import java.util.OptionalInt;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@Slf4j
//@SpringBootTest
//@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = "/drop.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//public class RepositoryTest {
//
//    @Autowired
//    private UserProfileRepository userProfileRepository;
//
//    @Autowired
//    private ImportRepository importRepository;
//
//    @Autowired
//    private TranslationRepository translationRepository;
//
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            "postgres:latest"
//    );
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.login", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }
//
//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//    }
//
//    @AfterAll
//    static void afterAll() {
//        postgres.stop();
//    }
//
//    @Test
//    void shouldCreateEntities() {
//        Optional<UserProfile> userProfile = testLoadUserProfile();
//        assertEquals(1, userProfileRepository.findAll().size());
//
//        userProfile.ifPresent(profile -> {
//            ImportDetails importDetails = ImportFactory.createImportDetails(
//                    "very profound title",
//                    "plenty of content.jte here",
//                    "no url"
//            );
//            OptionalInt importId = importRepository.addImport(
//                    importDetails,
//                    profile.userProfileSettings(),
//                    profile.userProfileCredentials().username()
//            );
//            assertEquals(1, importRepository.findAll().size());
//
//            importId.ifPresent(id -> {
//                testAddMultipleTranslations(profile.userProfileSettings(), profile.userProfileCredentials().username(), id);
//                assertEquals(5, translationRepository.findAll().size());
//                assertEquals(5, translationRepository.findByOwner(profile.userProfileCredentials().username()).size());
//
//                log.info("""
//                        User Profile created: {}
//                        With new import (id): {}
//                        """, profile, id);
//            });
//        });
//    }
//    @Test
//    void shouldModifyTranslations() {
//        setupTestEntities();
//        translationRepository.updateFamiliarity("en", Familiarity.FAMILIAR);
//        translationRepository.updateFamiliarity("enen", Familiarity.RECOGNIZED);
//        assertEquals(Familiarity.FAMILIAR, translationRepository.findByTargetWord("en").get().translationDetails().familiarity());
//        assertEquals(Familiarity.RECOGNIZED, translationRepository.findByTargetWord("enen").get().translationDetails().familiarity());
//
//        Optional<Translation> translation1 = translationRepository.updateSourceWord("jeden", "en");
//        Optional<Translation> translation2 = translationRepository.updateSourceWord("dwa", "enen");
//        assertEquals("jeden", translation1.get().translationDetails().sourceWord());
//        assertEquals("dwa", translation2.get().translationDetails().sourceWord());
//    }
//
//    private void setupTestEntities() {
//        Optional<UserProfile> userProfile = testLoadUserProfile();
//        userProfile.ifPresent(profile -> {
//            int importId = testAddImport(profile.userProfileSettings(), profile.userProfileCredentials().username());
//            testAddMultipleTranslations(profile.userProfileSettings(), profile.userProfileCredentials().username(), importId);
//        });
//    }
//
//    private Optional<UserProfile> testLoadUserProfile() {
//        UserProfileCredentials credentials = UserProfileFactory.createCredentials(
//                "login",
//                "email@email.com",
//                "password"
//        );
//        UserProfileSettings settings = UserProfileFactory.createSettings(
//                Language.EN,
//                Language.NO
//        );
//        return userProfileRepository.addUserProfile(credentials, settings);
//    }
//
//    private int testAddImport(UserProfileSettings userProfileSettings, String owner) {
//        OptionalInt addedImport = importRepository.addImport(
//                ImportFactory.createImportDetails(
//                        "title",
//                        "content.jte",
//                        "url"
//                ),
//                userProfileSettings,
//                owner
//        );
//        return addedImport.orElseThrow(() -> new RuntimeException("An exception has been caught while adding an import"));
//    }
//
//    private void testAddMultipleTranslations(UserProfileSettings userProfileSettings, String owner, int importId) {
//        for(int i = 1; i <= 5; i++) {
//            TranslationDetails translationDetails = TranslationFactory.createTranslationDetails(
//                    "one".repeat(i),
//                    "en".repeat(i),
//                    Familiarity.UNKNOWN
//            );
//            translationRepository.addTranslation(
//                    translationDetails,
//                    userProfileSettings,
//                    owner,
//                    importId
//            ).orElseThrow(() -> new RuntimeException("An exception has been caught while adding a translation"));
//        }
//    }
//}
