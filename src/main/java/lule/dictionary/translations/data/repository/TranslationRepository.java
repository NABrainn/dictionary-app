package lule.dictionary.translations.data.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.language.service.Language;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.Translation;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TranslationRepository {

    private final JdbcTemplate template;
    private final RowMapper<Translation> translationMapper = (rs, rowNum) -> Translation.builder()
            .sourceWords(Arrays.asList((String[]) rs.getArray("source_words").getArray()))
            .targetWord(rs.getString("target_word"))
            .familiarity(Familiarity.valueOf(rs.getString("familiarity")))
            .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
            .targetLanguage(Language.valueOf(rs.getString("target_lang")))
            .owner(rs.getString("translation_owner"))
            .isPhrase(rs.getBoolean("is_phrase"))
            .unprocessedTargetWord("")
            .build();
    private final RowMapper<String> sourceWordsMapper = (rs, rowNum) -> rs.getString("word");
    private final RowMapper<Integer> translationIdMapper = (rs, rowNum) -> rs.getInt("translations_id");

    public OptionalInt addTranslation(@NonNull Translation translation, int importId) {
        String insertSql = """
            WITH inserted_translation AS (
                INSERT INTO dictionary.translations (
                    source_words, target_word, source_lang, target_lang, translation_owner, familiarity, is_phrase
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
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
                ps.setBoolean(7, translation.isPhrase());
                ps.setInt(8, importId);
                ps.setInt(9, 1);
                return ps;
            }, translationIdMapper).stream().findFirst().orElseThrow(() -> new RuntimeException("translation not found"));
            template.update(updateSql, translation.owner());
            if(translationId != null) {
                return OptionalInt.of(translationId);
            }
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Translation> updateSourceWords(List<String> sourceWords, String targetWord, String owner) {
        String sql = """
                UPDATE dictionary.translations
                SET source_words = ?
                WHERE target_word = ?
                AND translation_owner = ?
                RETURNING *
                """;
        try {
            return template.query(con -> {
                var ps = con.prepareStatement(sql);
                ps.setArray(1, con.createArrayOf("text", sourceWords.toArray()));
                ps.setString(2, targetWord);
                ps.setString(3, owner);
                return ps;
            }, translationMapper).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Translation> updateFamiliarity(Familiarity familiarity, String targetWord, String owner) {
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
            }, translationMapper).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Translation> findAll() {
        String sql = """
                SELECT *
                FROM dictionary.translations
                """;
        try {
            return template.query(sql, translationMapper);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Translation> findByTargetWord(String targetWord, String owner) {
        String sql = """
                SELECT *
                FROM dictionary.translations
                WHERE translations.target_word=?
                AND translation_owner=?
                LIMIT 1
                """;
        try {
            List<Translation> translation = template.query(sql, translationMapper,
                    targetWord,
                    owner);
            return translation.stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
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
                AND is_phrase = false
                """, placeholders);

        try {
            Object[] params = Stream.concat(
                    targetWords.stream(),
                    Stream.of(owner)
            ).toArray();
            return template.query(sql, translationMapper, params);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Translation> deleteSourceWord(String sourceWord, String targetWord, String owner) {
        String sql = """
                UPDATE dictionary.translations
                SET source_words = array_remove(source_words, ?)
                WHERE target_word = ?
                AND translation_owner = ?
                RETURNING *
                """;
        try {
            return template.query(sql, translationMapper,
                    sourceWord,
                    targetWord,
                    owner
            ).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
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
            return template.query(sql, sourceWordsMapper,
                    targetWord.toLowerCase(),
                    count);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Translation> extractPhrases(String content, String owner) {
        String sql = """
                SELECT DISTINCT *
                FROM dictionary.translations
                WHERE to_tsvector(lower(?)) @@ plainto_tsquery(lower(target_word))
                AND translation_owner = ?
                AND is_phrase = true
                """;
        try {
            return template.query(sql, translationMapper,
                    content,
                    owner);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Translation> getRandomTranslations(boolean isPhrase, String owner, int limit, int familiarity) {
        if (familiarity > 0 && familiarity <= 5) {
            String sql = """
                SELECT *
                FROM dictionary.translations
                WHERE is_phrase = ?
                AND translation_owner = ?
                AND familiarity = CAST(? AS dictionary.familiarity)
                ORDER BY RANDOM()
                LIMIT ?
                """;
            try {
                return template.query(sql, translationMapper,
                        isPhrase,
                        owner,
                        Familiarity.values()[familiarity - 1].name(),
                        limit);
            } catch (DataAccessException e) {
                log.error(String.valueOf(e.getCause()));
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        String sql = """
                SELECT *
                FROM dictionary.translations
                WHERE is_phrase = ?
                AND translation_owner = ?
                ORDER BY RANDOM()
                LIMIT ?
                """;
        try {
            return template.query(sql, translationMapper,
                    isPhrase,
                    owner,
                    limit);
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
