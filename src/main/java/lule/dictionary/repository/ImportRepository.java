package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.imports.base.Import;
import lule.dictionary.dto.application.interfaces.imports.ImportDetails;
import lule.dictionary.dto.application.interfaces.imports.base.ImportWithId;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;
import lule.dictionary.exception.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static lule.dictionary.factory.RowMapperFactory.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ImportRepository {

    private final JdbcTemplate template;

    public OptionalInt addImport(@NonNull ImportDetails importDetails, @NonNull UserProfileSettings userProfileSettings, @NonNull String owner) throws RepositoryException {
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


    public Optional<Import> findById(int id) throws RepositoryException {
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

    public List<ImportWithId> findByOwner(@NonNull String owner) throws RepositoryException {
        String sql = """
                SELECT *
                FROM dictionary.imports
                WHERE import_owner=?
                """;
        try {
            return template.query(sql, IMPORT_WITH_ID, owner);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Import> findAll() throws RepositoryException {
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
