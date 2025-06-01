package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileCredentials;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.exception.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static lule.dictionary.factory.RowMapperFactory.USER_PROFILE;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProfileRepository {

    private final JdbcTemplate template;

    public Optional<UserProfile> findByUsername(@NonNull String username) {
        String sql = """
                SELECT user_profiles.username, user_profiles.password, user_profiles.email, user_profile_settings.source_lang, user_profile_settings.target_lang
                FROM user_profiles, user_profile_settings
                LEFT JOIN user_profiles.settings_id ON user_profile_settings.settings_id
                WHERE username=?
                """;
        try {
            UserProfile result = template.queryForObject(sql, USER_PROFILE, username);
            return Optional.ofNullable(result);
        } catch (DataAccessException e) {
            throw new RepositoryException(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    public Optional<UserProfile> addUserProfile(@NonNull UserProfileCredentials userProfileCredentials, @NonNull UserProfileSettings userProfileSettings) throws RepositoryException {
        String sql = """
                    WITH settings AS (
                        INSERT INTO dictionary.user_profile_settings (source_lang, target_lang)
                        VALUES (?, ?)
                        RETURNING settings_id, source_lang, target_lang
                    )
                    INSERT INTO dictionary.user_profiles (username, email, password, settings_id)
                    VALUES (?, ?, ?, (SELECT settings_id FROM settings))
                    RETURNING username, email, password,
                        (SELECT source_lang FROM settings) AS source_lang,
                        (SELECT target_lang FROM settings) AS target_lang
                """;
        try {
            UserProfile userProfile = template.queryForObject(sql, USER_PROFILE,
                    userProfileSettings.sourceLanguage().toString(),
                    userProfileSettings.targetLanguage().toString(),
                    userProfileCredentials.username(),
                    userProfileCredentials.email(),
                    userProfileCredentials.password()
            );
            return Optional.ofNullable(userProfile);
        } catch (DataAccessException e) {
            throw new RepositoryException(e.getCause());
        }
    }

    public List<UserProfile> findAll() throws RepositoryException {
        String sql = """
                SELECT p.username, p.email, p.password, s.source_lang, s.target_lang
                FROM dictionary.user_profiles as p
                LEFT JOIN dictionary.user_profile_settings as s
                ON p.settings_id=s.settings_id
                """;
        try {
            return template.query(sql, USER_PROFILE);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
