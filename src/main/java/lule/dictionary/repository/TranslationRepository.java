package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.exception.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static lule.dictionary.repository.factory.RowMapperFactory.TRANSLATION;
import static lule.dictionary.repository.factory.RowMapperFactory.TRANSLATION_ID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TranslationRepository {

    private final JdbcTemplate template;

    @Transactional
    public OptionalInt addTranslation(@NonNull Translation translation, int importId) throws RepositoryException {
        String sql = """
                WITH translation AS (
                    INSERT INTO dictionary.translations (source_words, target_word, source_lang, target_lang, translation_owner, familiarity)
                    VALUES (?, ?, ?, ?, ?, ?)
                    RETURNING translations_id
                )
                INSERT INTO dictionary.imports_translations (imports_id, translations_id, amount)
                VALUES (?, (SELECT translations_id FROM translation), ?)
                RETURNING (SELECT translations_id FROM translation)
                """;
        try {
            Integer translationId = template.query(con -> {
                PreparedStatement ps = con.prepareStatement(sql);
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
            if(translationId != null) {
                return OptionalInt.of(translationId);
            }
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.info(String.valueOf(e.getCause()));
            throw new RepositoryException(e.getCause());
        }
    }

    public Optional<Translation> updateSourceWords(@NonNull List<String> sourceWords, @NonNull String targetWord, @NonNull String owner) throws RepositoryException {
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
            log.error(e.getMessage(), e.getCause());
            throw new RepositoryException(e.getCause());
        }
    }

    public Optional<Translation> updateFamiliarity(@NonNull String targetWord, @NonNull Familiarity familiarity, @NonNull String owner) throws RepositoryException {
        String sql = """
                UPDATE dictionary.translations
                SET familiarity = ?
                WHERE target_word = ?
                AND translation_owner = ?
                RETURNING *
                """;
        try {
            Optional<Translation> translation = template.query(con -> {
                var ps = con.prepareStatement(sql);
                ps.setString(1, familiarity.name());
                ps.setString(2, targetWord);
                ps.setString(3, owner);
                return ps;
            }, TRANSLATION).stream().findFirst();
            return translation;
        } catch (DataAccessException e) {
            log.error(e.getMessage(), e.getCause());
            throw new RepositoryException(e.getCause());
        }
    }

    public List<Translation> findByOwner(@NonNull String username) throws RepositoryException {
        String sql = """
                SELECT *
                FROM dictionary.translations
                WHERE translations.translation_owner=?
                """;

        try {
            return template.query(sql, TRANSLATION, username);
        } catch (DataAccessException e) {
            throw new RepositoryException(e.getCause());
        }
    }

    public List<Translation> findAll() throws RepositoryException {
        String sql = """
                SELECT *
                FROM dictionary.translations
                """;
        try {
            return template.query(sql, TRANSLATION);
        } catch (DataAccessException e) {
            throw new RepositoryException(e.getCause());
        }
    }

    public Optional<Translation> findByTargetWord(@NonNull String targetWord) throws RepositoryException {
        String sql = """
                SELECT *
                FROM dictionary.translations
                WHERE translations.target_word=?
                LIMIT 1
                """;
        try {
            List<Translation> translation = template.query(sql, TRANSLATION, targetWord.toLowerCase());
            return translation.stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new RepositoryException(e.getCause());
        }
    }

    public List<Translation> findByTargetWords(List<String> targetWords) {
        String sql = String.format("""
                SELECT *
                FROM dictionary.translations
                WHERE translations.target_word IN (%s)
                """, String.join(",", targetWords.stream().map(word -> "?").toArray(String[]::new)));

        try {
            String[] targetWordsArray = targetWords.toArray(new String[0]);
            return template.query(sql, TRANSLATION, (Object[]) targetWordsArray);
        } catch (DataAccessException e) {
            throw new RepositoryException(e.getCause());
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
                    owner).stream().findFirst();
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }
}
