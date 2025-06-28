package lule.dictionary.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.application.interfaces.imports.ImportWithPagination;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static lule.dictionary.repository.factory.RowMapperFactory.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ImportRepository {

    private final JdbcTemplate template;

    public OptionalInt addImport(Import importt) {
        final String sql = """
                INSERT INTO dictionary.imports (title, content, url, source_lang, target_lang, import_owner)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING imports_id
                """;
        try {
            List<Integer> importsId = template.query(sql, IMPORT_ID,
                    importt.title(),
                    importt.content(),
                    importt.url(),
                    importt.sourceLanguage().toString(),
                    importt.targetLanguage().toString(),
                    importt.owner());

            if(importsId.stream().findFirst().isPresent())
                return OptionalInt.of(importsId.stream().findFirst().get());
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.warn(e.getMessage());
            return OptionalInt.empty();
        }
    }


    public Optional<ImportWithPagination> findById(int id, int page) {
        String sql = """
                SELECT
                    imports_id,
                    title,
                    content,
                    url,
                    source_lang,
                    target_lang,
                    import_owner,
                    substring(
                        content
                        FROM ((? - 1) * 2000 + 1)
                        FOR 2000
                    ) as page_content
                FROM dictionary.imports
                WHERE imports.imports_id=?
                """;
        try {
            List<ImportWithPagination> found = template.query(sql, IMPORT_WITH_PAGINATION,
                    page,
                    id
            );
            return found.stream().findFirst();
        } catch (DataAccessException e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    public List<ImportWithId> findByOwner(@NonNull String owner) {
        String sql = """
                SELECT *
                FROM dictionary.imports
                WHERE import_owner=?
                """;
        try {
            return template.query(sql, IMPORT_WITH_ID, owner);
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            return List.of();
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
            log.warn(e.getMessage());
            return List.of();
        }
    }
}
