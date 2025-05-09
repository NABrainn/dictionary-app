package lule.dictionary.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.Translation;
import lule.dictionary.factory.TranslationFactory;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TranslationRepository {

    private final JdbcTemplate template;

    public OptionalInt addTranslation(Translation translation, int importId) throws RepositoryException {
        final RowMapper<Integer> ROW_MAPPER = ((rs, rowNum) -> rs.getInt("translations_id"));
        String translationsInsertSql = "INSERT INTO dictionary.translations (source_word, target_word, source_lang, target_lang, translation_owner, familiarity) VALUES (?, ?, ?, ?, ?, ?) RETURNING translations_id";
        String relationsInsertSql = "INSERT INTO dictionary.imports_translations (imports_id, translations_id, amount) VALUES (?, ?, ?)";
        try {
            Integer translationId = template.queryForObject(translationsInsertSql,
                    ROW_MAPPER,
                    translation.sourceWord(),
                    translation.targetWord(),
                    translation.sourceLanguage().toString().toLowerCase(),
                    translation.targetLanguage().toString().toLowerCase(),
                    translation.translationOwner(),
                    translation.familiarity().toString().toLowerCase()
            );
            if(translationId != null) {
                template.update(relationsInsertSql, importId, translationId, 1);
                return OptionalInt.of(translationId);
            }
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage());
        }
    }

    public List<Translation> findAllByImport(int importId) throws RepositoryException {
        final RowMapper<Translation> ROW_MAPPER = ((rs, rowNum) -> new Translation(
                rs.getString("source_word"),
                rs.getString("target_word"),
                Language.valueOf(rs.getString("source_lang").toUpperCase()),
                Language.valueOf(rs.getString("target_lang").toUpperCase()),
                rs.getString("translation_owner"),
                Familiarity.valueOf(rs.getString("familiarity").toUpperCase())
        ));
        String sql = """
                SELECT t.source_word, t.target_word, t.source_lang, t.target_lang, t.translation_owner, t.familiarity
                FROM dictionary.translations t
                LEFT JOIN dictionary.imports_translations it ON t.translations_id = it.translations_id
                WHERE it.imports_id=?;
                """;
        try {
            return template.query(sql, ROW_MAPPER, importId);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage());
        }
    }

    public Optional<Translation> findByTargetWord(String targetWord) throws RepositoryException {
        RowMapper<Translation> ROW_MAPPER = (rs, rowNum) -> TranslationFactory.create(
                rs.getString("source_word"),
                rs.getString("target_word"),
                Language.valueOf(rs.getString("source_lang").toUpperCase()),
                Language.valueOf(rs.getString("target_lang").toUpperCase()),
                rs.getString("translation_owner"),
                Familiarity.valueOf(rs.getString("familiarity").toUpperCase())
        );
        String sql = "SELECT * FROM dictionary.translations WHERE target_word=?";
        try {
            Translation result = template.queryForObject(sql, ROW_MAPPER, targetWord);
            return Optional.ofNullable(result);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage());
        }
    }

    public void updateSourceWord(Translation translation, String sourceWord) throws RepositoryException {
        String sql = "UPDATE dictionary.translations SET source_word = ? WHERE target_word = ?";
        try {
            template.update(sql, sourceWord, translation.targetWord());
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage());
        }
    }

    public void updateFamiliarity(Translation translation, Familiarity familiarity) throws RepositoryException {
        String sql = "UPDATE dictionary.translations SET familiarity = ? WHERE target_word = ?";
        try {
            template.update(sql, familiarity.toString().toLowerCase(), translation.targetWord());
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage());
        }
    }
}
