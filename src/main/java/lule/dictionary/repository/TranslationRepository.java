package lule.dictionary.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.Translation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    private final RowMapper<Integer> TRANSLATIONS_ID_MAPPER = ((rs, rowNum) -> rs.getInt("translations_id"));
//    private final RowMapper<Translation> TRANSLATION_MAPPER = ((rs, rowNum) -> new Translation(rs.getString(), ));

    public OptionalInt addTranslation(Translation translation, int importId) throws RuntimeException {
        String translationsInsertSql = "INSERT INTO dictionary.translations (source_word, target_word, source_lang, target_lang, translation_owner, familiarity) VALUES (?, ?, ?, ?, ?, ?) RETURNING translations_id";
        String relationsInsertSql = "INSERT INTO dictionary.imports_translations (imports_id, translations_id, amount) VALUES (?, ?, ?)";
        try {
            Integer translationId = template.queryForObject(translationsInsertSql,
                    TRANSLATIONS_ID_MAPPER,
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
            throw new RuntimeException(e);
        }
    }

    public List<Translation> findAllByImport(int importId) {
        String sql = """
                SELECT *
                FROM dictionary.translations t
                LEFT JOIN dictionary.imports_translations it ON t.translations_id = it.translations_id
                WHERE it.imports_id=?;
                """;
        try {
            return template.query(sql, new BeanPropertyRowMapper<>(Translation.class), importId);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Optional<Translation> findByTargetWord(String targetWord) {
        String sql = "SELECT * FROM dictionary.translations WHERE target_word = ?";
        try {
            Translation result = template.queryForObject(sql, Translation.class, targetWord);
            return Optional.ofNullable(result);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
