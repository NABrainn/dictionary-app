package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.dto.application.interfaces.translation.TranslationDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.exception.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static lule.dictionary.factory.RowMapperFactory.TRANSLATION;
import static lule.dictionary.factory.RowMapperFactory.TRANSLATION_ID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TranslationRepository {

    private final JdbcTemplate template;

    @Transactional
    public OptionalInt addTranslation(@NonNull Translation translation, int importId) throws RepositoryException {
        String sql = """
                WITH translation AS (
                    INSERT INTO dictionary.translations (source_word, target_word, source_lang, target_lang, translation_owner, familiarity)
                    VALUES (?, ?, ?, ?, ?, ?)
                    ON CONFLICT ON CONSTRAINT unique_translations_per_owner
                    DO UPDATE SET
                        source_lang = EXCLUDED.source_lang,
                        target_lang = EXCLUDED.target_lang,
                        familiarity = EXCLUDED.familiarity
                    RETURNING translations_id
                )
                INSERT INTO dictionary.imports_translations (imports_id, translations_id, amount)
                VALUES (?, (SELECT translations_id FROM translation), ?)
                RETURNING (SELECT translations_id FROM translation)
                """;
        try {
            Integer translationId = template.queryForObject(sql, TRANSLATION_ID,
                    translation.sourceWord(),
                    translation.targetWord().toLowerCase(),
                    translation.sourceLanguage().toString(),
                    translation.targetLanguage().toString(),
                    translation.owner(),
                    translation.familiarity().toString(),
                    importId,
                    1
            );
            if(translationId != null) {
                return OptionalInt.of(translationId);
            }
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.info(String.valueOf(e.getCause()));
            throw new RepositoryException(e.getCause());
        }
    }

    public Optional<Translation> updateSourceWord(@NonNull String sourceWord, @NonNull String targetWord) throws RepositoryException {
        String sql = """
                UPDATE dictionary.translations
                SET source_word = ?
                WHERE target_word = ?
                RETURNING *
                """;
        try {
            Translation translation = template.queryForObject(sql, TRANSLATION,
                    sourceWord,
                    targetWord
            );
            return Optional.ofNullable(translation);
        } catch (DataAccessException e) {
            throw new RepositoryException(e.getCause());
        }
    }

    public Optional<Translation> updateFamiliarity(@NonNull String targetWord, @NonNull Familiarity familiarity) throws RepositoryException {
        String sql = """
                UPDATE dictionary.translations
                SET familiarity = ?
                WHERE target_word = ?
                RETURNING *
                """;
        try {
            Translation translation = template.queryForObject(sql, TRANSLATION,
                    familiarity.name(),
                    targetWord.toLowerCase()
            );
            return Optional.ofNullable(translation);
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
}
