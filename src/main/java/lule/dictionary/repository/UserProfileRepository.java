package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static lule.dictionary.repository.factory.RowMapperFactory.USER_PROFILE;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProfileRepository {

    private final JdbcTemplate template;

    public Optional<UserProfile> findByUsername(@NonNull String username) {
        String sql = """
                    SELECT
                        p.username,
                        p.password,
                        p.email,
                        s.source_lang,
                        s.target_lang,
                        s.ui_lang,
                        str.day_count,
                        str.words_added_today,
                        str.tz_offset,
                        str.updated_at
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
                    SELECT
                        p.username, 
                        p.password, 
                        p.email, 
                        s.source_lang, 
                        s.target_lang, 
                        s.ui_lang, 
                        str.day_count, 
                        str.words_added_today, 
                        str.tz_offset, 
                        str.updated_at
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
                        INSERT INTO dictionary.user_profile_settings (source_lang, target_lang, ui_lang)
                        VALUES (?, ?, ?)
                        RETURNING settings_id, source_lang, target_lang, ui_lang
                    ),
                    streak AS (
                        INSERT INTO dictionary.streaks (day_count, words_added_today, streak_owner, tz_offset, updated_at)
                        VALUES (0, 0, ?, ?, '2025-07-02 10:00:00+02')
                        RETURNING day_count, words_added_today, streak_owner, tz_offset, updated_at
                    ),
                    user_insert AS (
                        INSERT INTO dictionary.user_profiles (username, email, password, settings_id)
                        SELECT ?, ?, ?, s.settings_id
                        FROM settings s
                        RETURNING username, email, password, settings_id
                    )
                    SELECT u.username, u.email, u.password, s.source_lang, s.target_lang, s.ui_lang, str.day_count, str.words_added_today, str.tz_offset, str.updated_at
                    FROM user_insert u
                    LEFT JOIN settings s ON u.settings_id = s.settings_id
                    LEFT JOIN streak str ON u.username = str.streak_owner;
                """;
        try {
            List<UserProfile> addedUser = template.query(sql, USER_PROFILE,
                    userProfile.sourceLanguage().name(),
                    userProfile.targetLanguage().name(),
                    userProfile.userInterfaceLanguage().name(),
                    userProfile.username(),
                    0,
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
                    SELECT
                        p.username,
                        p.email,
                        p.password,
                        s.source_lang,
                        s.target_lang,
                        s.ui_lang,
                        str.day_count,
                        str.words_added_today,
                        str.tz_offset,
                        str.updated_at
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

    public void updateTimezoneOffset(String owner, String offset) {
        String sql = """
                    UPDATE dictionary.streaks
                    SET tz_offset=?
                    WHERE streak_owner=?
                """;
        try {
            template.update(sql,
                    offset,
                    owner);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
        }
    }

    public void resetStreaksIfMidnight() {
        String resetSql = """
                            UPDATE dictionary.streaks
                            SET
                                words_added_today = 0,
                                day_count = 0,
                                updated_at = now()
                            WHERE
                                date_trunc('day', updated_at + (tz_offset)::interval) <
                                date_trunc('day', now() + (tz_offset)::interval)
                                AND words_added_today < 50
        """;

        String incrementSql = """
                            UPDATE dictionary.streaks
                            SET
                                words_added_today = 0,
                                day_count = day_count + 1,
                                updated_at = now()
                            WHERE
                                date_trunc('day', updated_at + (tz_offset)::interval) <
                                date_trunc('day', now() + (tz_offset)::interval)
                                AND words_added_today >= 50
        """;
        template.update(resetSql);
        template.update(incrementSql);
    }

    public OptionalInt getDailyStreak(String owner) {
        String sql = """
                    SELECT day_count
                    FROM dictionary.streaks
                    WHERE streak_owner=?
                """;
        try {
            Integer result = template.queryForObject(sql, Integer.class, owner);
            if(result != null) return OptionalInt.of(result);
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return OptionalInt.empty();
        }
    }

    public void updateTargetLanguage(String owner, String targetLanguage) {
        String sql = """
            UPDATE dictionary.user_profile_settings
            SET target_lang = ?
            WHERE settings_id = (
                SELECT settings_id
                FROM dictionary.user_profiles
                WHERE username = ?
            )
        """;
        try {
            template.update(sql,
                    targetLanguage,
                    owner);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
        }
    }

    public void updateSourceLanguage(String owner, String targetLanguage) {
        String sql = """
            UPDATE dictionary.user_profile_settings
            SET source_lang = ?
            WHERE settings_id = (
                SELECT settings_id
                FROM dictionary.user_profiles
                WHERE username = ?
            )
        """;
        try {
            template.update(sql,
                    targetLanguage,
                    owner);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
        }
    }

    public void updateUILanguage(String owner, String targetLanguage) {
        String sql = """
            UPDATE dictionary.user_profile_settings
            SET ui_lang = ?
            WHERE settings_id = (
                SELECT settings_id
                FROM dictionary.user_profiles
                WHERE username = ?
            )
        """;
        try {
            template.update(sql,
                    targetLanguage,
                    owner);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
        }
    }
}
