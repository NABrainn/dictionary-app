package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static lule.dictionary.repository.factory.RowMapperFactory.USER_PROFILE;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProfileRepository {

    private final JdbcTemplate template;

    public Optional<UserProfile> findByUsername(@NonNull String username) {
        String sql = """
                    SELECT p.username, p.password, p.email, s.source_lang, s.target_lang, str.day_count, str.is_daily_goal_met, str.updated_at
                    FROM dictionary.user_profiles p
                    LEFT JOIN dictionary.user_profile_settings s ON p.settings_id=s.settings_id
                    LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner
                    WHERE p.username=?;
                """;
        try {
            List<UserProfile> result = template.query(sql, USER_PROFILE, username);
            return result.stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public Optional<UserProfile> findByUsernameOrEmail(@NonNull String username, @NonNull String email) {
        String sql = """
                    SELECT p.username, p.password, p.email, s.source_lang, s.target_lang, str.day_count, str.is_daily_goal_met, str.updated_at
                    FROM dictionary.user_profiles p
                    LEFT JOIN dictionary.user_profile_settings s ON p.settings_id=s.settings_id
                    LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner
                    WHERE p.username=? OR p.email=?;
                """;
        try {
            List<UserProfile> result = template.query(sql, USER_PROFILE, username, email);
            return result.stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public Optional<UserProfile> addUserProfile(@NonNull UserProfile userProfile) {
        String sql = """
                    WITH settings AS (
                        INSERT INTO dictionary.user_profile_settings (source_lang, target_lang)
                        VALUES (?, ?)
                        RETURNING settings_id, source_lang, target_lang
                    ),
                    streak AS (
                        INSERT INTO dictionary.streaks (day_count, is_daily_goal_met, streak_owner, updated_at)
                        VALUES (0, false, 'useruseruser', '2025-07-02 15:00:00+02')
                        RETURNING day_count, is_daily_goal_met, streak_owner, updated_at
                    ),
                    user_insert AS (
                        INSERT INTO dictionary.user_profiles (username, email, password, settings_id)
                        SELECT ?, ?, ?, s.settings_id
                        FROM settings s
                        RETURNING username, email, password, settings_id
                    )
                    SELECT u.username, u.email, u.password, s.source_lang, s.target_lang, str.day_count, str.is_daily_goal_met, str.updated_at
                    FROM user_insert u
                    LEFT JOIN settings s ON u.settings_id = s.settings_id
                    LEFT JOIN streak str ON u.username = str.streak_owner;
                """;
        try {
            List<UserProfile> addedUser = template.query(sql, USER_PROFILE,
                    userProfile.sourceLanguage().name(),
                    userProfile.targetLanguage().name(),
                    userProfile.username(),
                    userProfile.email(),
                    userProfile.password()
            );
            return addedUser.stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public List<UserProfile> findAll() {
        String sql = """
                    SELECT p.username, p.email, p.password, s.source_lang, s.target_lang, str.day_count, str.is_daily_goal_met, str.updated_at
                    FROM dictionary.user_profiles as p
                    LEFT JOIN dictionary.user_profile_settings s ON p.settings_id=s.settings_id
                    LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner
                """;
        try {
            return template.query(sql, USER_PROFILE);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            return List.of();
        }
    }
}
