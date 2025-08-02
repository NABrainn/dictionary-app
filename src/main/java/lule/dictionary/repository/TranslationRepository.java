package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.translation.dto.request.DeleteSourceWordRequest;
import lule.dictionary.service.translation.dto.request.FindByTargetWordRequest;
import lule.dictionary.service.translation.dto.request.UpdateSourceWordsRequest;
import lule.dictionary.service.translation.dto.request.UpdateTranslationFamiliarityRequest;
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
                AND streak_owner=?
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
            template.update(updateSql, translation.owner());
            if(translationId != null) {
                return OptionalInt.of(translationId);
            }
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return OptionalInt.empty();
        }
    }

    public Optional<Translation> updateSourceWords(UpdateSourceWordsRequest request) {
        String sql = """
                UPDATE dictionary.translations
                SET source_words =  ?
                WHERE target_word = ?
                AND translation_owner = ?
                RETURNING *
                """;
        try {
            return template.query(con -> {
                var ps = con.prepareStatement(sql);
                ps.setArray(1, con.createArrayOf("text", request.sourceWords().toArray()));
                ps.setString(2, request.targetWord());
                ps.setString(3, request.owner());
                return ps;
            }, TRANSLATION).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public Optional<Translation> updateFamiliarity(UpdateTranslationFamiliarityRequest request) {
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
                ps.setString(1, request.familiarity().name());
                ps.setString(2, request.targetWord());
                ps.setString(3, request.owner());
                return ps;
            }, TRANSLATION).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
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

    public Optional<Translation> findByTargetWord(FindByTargetWordRequest request) {
        String sql = """
                SELECT *
                FROM dictionary.translations
                WHERE translations.target_word=?
                AND translation_owner=?
                LIMIT 1
                """;
        try {
            List<Translation> translation = template.query(sql, TRANSLATION,
                    request.targetWord().toLowerCase(),
                    request.owner());
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

    public Optional<Translation> deleteSourceWord(DeleteSourceWordRequest request) {
        String sql = """
                UPDATE dictionary.translations
                SET source_words = array_remove(source_words, ?)
                WHERE target_word = ?
                AND translation_owner = ?
                RETURNING *
                """;
        try {
            return template.query(sql, TRANSLATION,
                    request.sourceWord(),
                    request.targetWord(),
                    request.owner()
            ).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return Optional.empty();
        }
    }

    public int getWordsLearnedCount(String owner, Language targetLanguage) {
        String sql = """
            SELECT COUNT(*)
            FROM dictionary.translations
            WHERE translation_owner = ?
            AND target_lang = CAST(? AS dictionary.lang)
            AND familiarity != CAST(? AS dictionary.familiarity)
        """;

        try {
            Integer count = template.queryForObject(sql, Integer.class,
                    owner,
                    targetLanguage.name(),
                    Familiarity.IGNORED.name()
            );
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

    public List<String> extractPhrases(String content, String owner) {
        String sql = """
                SELECT target_word
                FROM dictionary.translations
                WHERE position(target_word in ?) > 0
                AND translation_owner = ?
                AND is_phrase = true
                """;
        try {
            return template.query(sql, PHRASES,
                    content,
                    owner);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            return List.of();
        }
    }
}
