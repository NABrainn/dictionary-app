package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.Import;
import lule.dictionary.factory.ImportFactory;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.OptionalInt;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ImportRepository {

    private final JdbcTemplate template;

    public OptionalInt addImport(Import imported) throws RepositoryException {
        RowMapper<Integer> ROW_MAPPER = ((rs, rowNum) -> rs.getInt("imports_id"));
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
            throw new RepositoryException(e.getMessage());
        }
    }

    public void deleteById(int id) throws RepositoryException{
        String sql = "DELETE FROM dictionary.imports WHERE imports_id=?";
        try {
            template.update(sql, id);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage());
        }
    }

    public List<Import> findAllByOwner(@NonNull String username) throws RepositoryException {
        RowMapper<Import> ROW_MAPPER = (rs, rowNum) -> ImportFactory.create(
                rs.getString("title"),
                rs.getString("content"),
                rs.getString("url"),
                Language.valueOf(rs.getString("source_lang").toUpperCase()),
                Language.valueOf(rs.getString("target_lang").toUpperCase()),
                rs.getString("import_owner")
        );
        String sql = "SELECT * FROM dictionary.imports WHERE import_owner=?";
        try {
            return template.query(sql, ROW_MAPPER, username);
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
