package lule.dictionary.userProfiles.data.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.language.service.Language;
import lule.dictionary.userProfiles.data.UserProfile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProfileRepository {

    private final JdbcTemplate template;
    private final RowMapper<UserProfile> userProfileMapper = ((rs, rowNum) ->
            UserProfile.builder()
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .userInterfaceLanguage(Language.valueOf(rs.getString("ui_lang")))
                    .wordsAddedToday(rs.getInt("words_added_today"))
                    .offset(rs.getString("tz_offset"))
                    .dailyStreak(rs.getInt("day_count"))
                    .isNavbarOpen(rs.getBoolean("is_navbar_open"))
                    .build());

    public Optional<UserProfile> findByUsername(@NonNull String username) {
        String sql = """
                    SELECT
                        p.username,
                        p.password,
                        p.email,
                        s.source_lang,
                        s.target_lang,
                        s.ui_lang,
                        s.is_navbar_open,
                        str.day_count,
                        str.words_added_today,
                        str.tz_offset,
                        str.updated_at
                    FROM dictionary.users p
                    LEFT JOIN dictionary.profile_settings s ON p.settings_id=s.settings_id
                    LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner
                    WHERE p.username=?;
                """;
        try {
            List<UserProfile> result = template.query(sql, userProfileMapper, username);
            return result.stream().findFirst();
        } catch (DataAccessException e) {
            log.error("Error in findByUsername for username: {}, cause: {}", username, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to find user by username");
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
                        s.is_navbar_open,
                        str.day_count,
                        str.words_added_today,
                        str.tz_offset,
                        str.updated_at
                    FROM dictionary.users p
                    LEFT JOIN dictionary.profile_settings s ON p.settings_id=s.settings_id
                    LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner
                    WHERE p.username=? OR p.email=?;
                """;
        try {
            List<UserProfile> result = template.query(sql, userProfileMapper, username, email);
            return result.stream().findFirst();
        } catch (DataAccessException e) {
            log.error("Error in findByUsernameOrEmail for username: {}, email: {}, cause: {}", username, email, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to find user by username or email");
        }
    }

    public Optional<UserProfile> addUserProfile(@NonNull UserProfile userProfile) {
        String sql = """
                    WITH settings AS (
                        INSERT INTO dictionary.profile_settings (source_lang, target_lang, ui_lang, is_navbar_open)
                        VALUES (?, ?, ?, ?)
                        RETURNING settings_id, source_lang, target_lang, ui_lang, is_navbar_open
                    ),
                    streak AS (
                        INSERT INTO dictionary.streaks (day_count, words_added_today, streak_owner, tz_offset, updated_at)
                        VALUES (0, 0, ?, ?, '2025-07-02 10:00:00+02')
                        RETURNING day_count, words_added_today, streak_owner, tz_offset, updated_at
                    ),
                    user_insert AS (
                        INSERT INTO dictionary.users (username, email, password, settings_id)
                        SELECT ?, ?, ?, s.settings_id
                        FROM settings s
                        RETURNING username, email, password, settings_id
                    )
                    SELECT
                        u.username,
                        u.email,
                        u.password,
                        s.source_lang,
                        s.target_lang,
                        s.ui_lang,
                        s.is_navbar_open,
                        str.day_count,
                        str.words_added_today,
                        str.tz_offset,
                        str.updated_at
                    FROM user_insert u
                    LEFT JOIN settings s ON u.settings_id = s.settings_id
                    LEFT JOIN streak str ON u.username = str.streak_owner;
                """;
        try {
            List<UserProfile> addedUser = template.query(sql, userProfileMapper,
                    userProfile.sourceLanguage().name(),
                    userProfile.targetLanguage().name(),
                    userProfile.userInterfaceLanguage().name(),
                    false,
                    userProfile.getUsername(),
                    0,
                    userProfile.getUsername(),
                    userProfile.email(),
                    userProfile.getPassword()
            );
            return addedUser.stream().findFirst();
        } catch (DataAccessException e) {
            log.error("Error in addUserProfile for username: {}, email: {}, cause: {}",
                    userProfile.getUsername(), userProfile.email(), e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add user profile");
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
                        is_navbar_open,
                        str.day_count,
                        str.words_added_today,
                        str.tz_offset,
                        str.updated_at
                    FROM dictionary.users as p
                    LEFT JOIN dictionary.profile_settings s ON p.settings_id=s.settings_id
                    LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner
                """;
        try {
            return template.query(sql, userProfileMapper);
        } catch (DataAccessException e) {
            log.error("Error in findAll, cause: {}", e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve all user profiles");
        }
    }

    public void updateTimezoneOffset(String owner, String offset) {
        String sql = """
                    UPDATE dictionary.streaks
                    SET tz_offset=?
                    WHERE streak_owner=?
                """;
        try {
            template.update(sql, offset, owner);
        } catch (DataAccessException e) {
            log.error("Error in updateTimezoneOffset for owner: {}, offset: {}, cause: {}", owner, offset, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update timezone offset");
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
        try {
            template.update(resetSql);
            template.update(incrementSql);
        } catch (DataAccessException e) {
            log.error("Error in resetStreaksIfMidnight, cause: {}", e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to reset streaks");
        }
    }

    public OptionalInt getDailyStreak(String owner) {
        String sql = """
                    SELECT day_count
                    FROM dictionary.streaks
                    WHERE streak_owner=?
                """;
        try {
            Integer result = template.queryForObject(sql, Integer.class, owner);
            if (result != null) return OptionalInt.of(result);
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.error("Error in getDailyStreak for owner: {}, cause: {}", owner, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve daily streak");
        }
    }

    public void updateTargetLanguage(String owner, String targetLanguage) {
        String sql = """
            UPDATE dictionary.profile_settings
            SET target_lang = ?
            WHERE settings_id = (
                SELECT settings_id
                FROM dictionary.users
                WHERE username = ?
            )
        """;
        try {
            template.update(sql, targetLanguage, owner);
        } catch (DataAccessException e) {
            log.error("Error in updateTargetLanguage for owner: {}, targetLanguage: {}, cause: {}",
                    owner, targetLanguage, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update target language");
        }
    }

    public void updateSourceLanguage(String owner, String sourceLanguage) {
        String sql = """
            UPDATE dictionary.profile_settings
            SET source_lang = ?
            WHERE settings_id = (
                SELECT settings_id
                FROM dictionary.users
                WHERE username = ?
            )
        """;
        try {
            template.update(sql, sourceLanguage, owner);
        } catch (DataAccessException e) {
            log.error("Error in updateSourceLanguage for owner: {}, sourceLanguage: {}, cause: {}",
                    owner, sourceLanguage, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update source language");
        }
    }

    public void updateUILanguage(String owner, String uiLanguage) {
        String sql = """
            UPDATE dictionary.profile_settings
            SET ui_lang = ?
            WHERE settings_id = (
                SELECT settings_id
                FROM dictionary.users
                WHERE username = ?
            )
        """;
        try {
            template.update(sql, uiLanguage, owner);
        } catch (DataAccessException e) {
            log.error("Error in updateUILanguage for owner: {}, uiLanguage: {}, cause: {}",
                    owner, uiLanguage, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update UI language");
        }
    }

    public boolean toggleNavbar(String username) {
        String sql = """
            WITH current_state AS (
                SELECT is_navbar_open
                FROM dictionary.profile_settings
                WHERE settings_id = (
                    SELECT settings_id
                    FROM dictionary.users
                    WHERE username = ?
                )
            )
            UPDATE dictionary.profile_settings
            SET is_navbar_open = COALESCE(NOT (SELECT is_navbar_open FROM current_state), false)
            WHERE settings_id = (
                SELECT settings_id
                FROM dictionary.users
                WHERE username = ?
            )
            RETURNING is_navbar_open
        """;
        try {
            return Optional.ofNullable(template.queryForObject(sql, Boolean.class, username, username))
                    .orElseThrow();
        } catch (EmptyResultDataAccessException e) {
            log.warn("No rows updated in toggleNavbar for username: {}, cause: {}", username, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or settings not found");
        } catch (DataAccessException e) {
            log.error("Error in toggleNavbar for username: {}, cause: {}", username, e.getCause(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not toggle navbar");
        }
    }
}