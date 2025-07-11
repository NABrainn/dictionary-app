package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static lule.dictionary.repository.factory.RowMapperFactory.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TranslationRepository {

    private final JdbcTemplate template;

    public OptionalInt addTranslation(@NonNull Translation translation, int importId) {
        String insertSql = """
            WITH inserted_translation AS (
                INSERT INTO dictionary.translations (
                    source_words, target_word, source_lang, target_lang, translation_owner, familiarity
                )
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING translations_id, translation_owner
            ),
            inserted_import AS (
                INSERT INTO dictionary.imports_translations (
                    imports_id, translations_id, amount
                )
                VALUES (?, (SELECT translations_id FROM inserted_translation), ?)
                RETURNING translations_id
            ),
            updated_streaks AS (
                UPDATE dictionary.streaks
                SET words_added_today = words_added_today + 1,
                    updated_at = now()
                WHERE streak_owner = (SELECT translation_owner FROM inserted_translation)
                RETURNING words_added_today, streak_owner, tz_offset, updated_at
            )
            SELECT translations_id FROM inserted_translation;
            """;
        String updateSql = """
                UPDATE dictionary.streaks
                SET day_count = day_count + 1
                WHERE words_added_today = 10
                """;
        try {
            Integer translationId = template.query(con -> {
                PreparedStatement ps = con.prepareStatement(insertSql);
                ps.setArray(1, con.createArrayOf("text", translation.sourceWords().toArray()));
                ps.setString(2, translation.targetWord().toLowerCase());
                ps.setString(3, translation.sourceLanguage().toString());
                ps.setString(4, translation.targetLanguage().toString());
                ps.setString(5, translation.owner());
                ps.setString(6, translation.familiarity().toString());
                ps.setInt(7, importId);
                ps.setInt(8, 1);
                return ps;
            }, TRANSLATION_ID).stream().findFirst().orElseThrow(() -> new RuntimeException("translation not found"));
            template.update(updateSql);
            if(translationId != null) {
                return OptionalInt.of(translationId);
            }
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return OptionalInt.empty();
        }
    }

    public Optional<Translation> updateSourceWords(@NonNull List<String> sourceWords, @NonNull String targetWord, @NonNull String owner) {
        String sql = """
                UPDATE dictionary.translations
                SET source_words =  ?
                WHERE target_word = ?
                AND translation_owner = ?
                RETURNING *
                """;
        try {
            Optional<Translation> translation = template.query(con -> {
                var ps = con.prepareStatement(sql);
                ps.setArray(1, con.createArrayOf("text", sourceWords.toArray()));
                ps.setString(2, targetWord);
                ps.setString(3, owner);
                return ps;
            }, TRANSLATION).stream().findFirst();
            return translation;
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public Optional<Translation> updateFamiliarity(@NonNull String targetWord, @NonNull Familiarity familiarity, @NonNull String owner) {
        String sql = """
                UPDATE dictionary.translations
                SET familiarity = ?
                WHERE target_word = ?
                AND translation_owner = ?
                RETURNING *
                """;
        try {
            return template.query(con -> {
                var ps = con.prepareStatement(sql);
                ps.setString(1, familiarity.name());
                ps.setString(2, targetWord);
                ps.setString(3, owner);
                return ps;
            }, TRANSLATION).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public List<Translation> findByOwner(@NonNull String username) {
        String sql = """
                SELECT *
                FROM dictionary.translations
                WHERE translations.translation_owner=?
                """;

        try {
            return template.query(sql, TRANSLATION, username);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return List.of();
        }
    }

    public List<Translation> findAll() {
        String sql = """
                SELECT *
                FROM dictionary.translations
                """;
        try {
            return template.query(sql, TRANSLATION);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return List.of();
        }
    }

    public Optional<Translation> findByTargetWord(@NonNull String targetWord,
                                                  String owner) {
        String sql = """
                SELECT *
                FROM dictionary.translations
                WHERE translations.target_word=?
                AND translation_owner=?
                LIMIT 1
                """;
        try {
            List<Translation> translation = template.query(sql, TRANSLATION,
                    targetWord.toLowerCase(),
                    owner);
            return translation.stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public List<Translation> findByTargetWords(List<String> targetWords, String owner) {
        if (targetWords.isEmpty()) {
            return List.of();
        }

        String placeholders = String.join(
                ",",
                targetWords.stream()
                        .map(w -> "?")
                        .toArray(String[]::new)
        );
        String sql = String.format("""
                SELECT *
                FROM dictionary.translations
                WHERE translations.target_word IN (%s)
                AND translation_owner = ?
                """, placeholders);

        try {
            Object[] params = Stream.concat(
                    targetWords.stream(),
                    Stream.of(owner)
            ).toArray();
            return template.query(sql, TRANSLATION, params);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return List.of();
        }
    }

    public Optional<Translation> deleteSourceWord(@NonNull String sourceWord, @NonNull String targetWord, @NonNull String owner) {
        String sql = """
                UPDATE dictionary.translations
                SET source_words = array_remove(source_words, ?)
                WHERE target_word = ?
                AND translation_owner = ?
                RETURNING *
                """;
        try {
            return template.query(sql, TRANSLATION,
                    sourceWord,
                    targetWord,
                    owner
            ).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public int getWordsLearnedCount(String owner) {
        String sql = """
            SELECT COUNT(*)
            FROM dictionary.translations
            WHERE translation_owner = ?
            AND familiarity != CAST(? AS dictionary.familiarity)
        """;

        try {
            Integer count = template.queryForObject(sql, Integer.class, owner, Familiarity.IGNORED.name());
            return count != null ? count : 0;
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return 0;
        }
    }

    public List<String> findMostFrequentSourceWords(String targetWord, int count) {
        String sql = """
                    SELECT word, COUNT(*) AS cunt
                    FROM (
                        SELECT unnest(source_words) AS word
                        FROM dictionary.translations
                        WHERE target_word = ?
                    ) AS sub
                    GROUP BY word
                    ORDER BY cunt DESC
                    LIMIT ?;
                """;
        try {
            return template.query(sql, SOURCE_WORDS,
                    targetWord.toLowerCase(),
                    count);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return List.of();
        }
    }
}
