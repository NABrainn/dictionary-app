package lule.dictionary.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalInt;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ImportRepository {

    private final JdbcTemplate template;

    private static final RowMapper<Integer> ROW_MAPPER = ((rs, rowNum) -> rs.getInt("imports_id"));

    public OptionalInt addImport(Import imported) {
        final String importsInsertSql = "INSERT INTO dictionary.imports (title, content, url, source_lang, target_lang, import_owner) VALUES (?, ?, ?, ?, ?, ?) RETURNING imports_id";
        try {
            Integer importsId = template.queryForObject(importsInsertSql,
                    ROW_MAPPER,
                    imported.title(),
                    imported.content(),
                    imported.url(),
                    imported.sourceLanguage().toString().toLowerCase(),
                    imported.targetLanguage().toString().toLowerCase(),
                    imported.owner());
            if(importsId != null) return OptionalInt.of(importsId);
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
