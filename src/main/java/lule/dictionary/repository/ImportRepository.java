package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.enumeration.Language;
import lule.dictionary.factory.dto.ImportFactory;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.factory.dto.UserProfileFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static lule.dictionary.factory.RowMapperFactory.IMPORT;
import static lule.dictionary.factory.RowMapperFactory.IMPORT_ID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ImportRepository {

    private final JdbcTemplate template;

    public OptionalInt addImport(ImportDetails importDetails, UserProfileSettings userProfileSettings, String owner) throws RepositoryException {
        final String sql = """
                INSERT INTO dictionary.imports (title, content, url, source_lang, target_lang, import_owner)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING imports_id
                """;
        try {
            Integer importsId = template.queryForObject(sql, IMPORT_ID,
                    importDetails.title(),
                    importDetails.content(),
                    importDetails.url(),
                    userProfileSettings.sourceLanguage().toString(),
                    userProfileSettings.targetLanguage().toString(),
                    owner);
            if(importsId != null) return OptionalInt.of(importsId);
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            throw new RepositoryException(e.getCause());
        }
    }

    public void deleteById(int id) throws RepositoryException{
        String sql = "DELETE FROM dictionary.imports WHERE imports_id=?";
        try {
            template.update(sql, id);
        } catch (DataAccessException e) {
            throw new RepositoryException(e.getCause());
        }
    }


    public Optional<Import> findById(int id) {
        String sql = """
                SELECT *
                FROM dictionary.imports
                WHERE imports.imports_id=?
                """;
        try {
            Import found = template.queryForObject(sql, IMPORT, id);
            return Optional.ofNullable(found);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Import> findAll() {
        String sql = """
                SELECT *
                FROM dictionary.imports
                """;
        try {
            return template.query(sql, IMPORT);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
