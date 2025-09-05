package lule.dictionary.documents.data.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.language.service.Language;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DocumentRepository {

    private final JdbcTemplate template;
    private final DocumentRowMapperStore rowMapperStore;

    public OptionalInt create(Document document) {
        final String sql = """
                INSERT INTO dictionary.imports (title, content, url, source_lang, target_lang, import_owner, total_length)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING imports_id
                """;
        try {
            List<Integer> documentId = template.query(sql, rowMapperStore.getDocumentIdMapper(),
                    document.title(),
                    document.pageContent(),
                    document.url(),
                    document.sourceLanguage().toString(),
                    document.targetLanguage().toString(),
                    document.owner(),
                    document.totalContentLength());

            if(documentId.stream().findFirst().isPresent())
                return OptionalInt.of(documentId.stream().findFirst().get());
            return OptionalInt.empty();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public Optional<Document> findById(int id, int page) {
        String sql = """
                SELECT
                    imports_id,
                    title,
                    url,
                    source_lang,
                    target_lang,
                    import_owner,
                    substring(
                        content
                        FROM ((? - 1) * 1000 + 1)
                        FOR 1000
                    ) as page_content,
                    total_length
                FROM dictionary.imports
                WHERE imports.imports_id=?
                """;
        try {
            List<Document> found = template.query(sql, rowMapperStore.getDocumentMapper(),
                    page, id);
            return found.stream().findFirst();
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<DocumentWithTranslationData> findByOwnerAndTargetLanguage(@NonNull String owner, @NonNull Language targetLanguage) {
        String sql = """
                WITH cleaned_words AS (
                    SELECT
                        i.imports_id,
                        i.title,
                        i.url,
                        i.source_lang,
                        i.target_lang,
                        i.import_owner,
                        ARRAY(
                            SELECT DISTINCT TRIM(LOWER(word))
                            FROM unnest(
                                REGEXP_SPLIT_TO_ARRAY(
                                    REGEXP_REPLACE(i.content, '[^[:alpha:][:space:]]', '', 'g'),
                                    '\\s+'
                                )
                            ) AS word
                            WHERE TRIM(word) != ''
                        ) AS word_array
                    FROM dictionary.imports i
                    WHERE i.import_owner = ?
                    AND i.target_lang = CAST(? AS dictionary.lang)
                ),
                relevant_translations AS (
                    SELECT DISTINCT t.target_word
                    FROM dictionary.translations t
                    WHERE t.translation_owner = ?
                    AND t.target_lang = CAST(? AS dictionary.lang)
                    AND t.familiarity IN ('UNKNOWN', 'RECOGNIZED', 'FAMILIAR')
                )
                SELECT
                    cw.title,
                    cw.url,
                    cw.source_lang,
                    cw.target_lang,
                    cw.import_owner,
                    cw.imports_id,
                    COALESCE(ARRAY_LENGTH(cw.word_array, 1), 0) AS word_count,
                    COALESCE(
                        (SELECT COUNT(DISTINCT word)
                         FROM unnest(cw.word_array) AS word
                         WHERE word NOT IN (SELECT target_word FROM relevant_translations)
                        ),
                        0
                    ) AS new_word_count,
                    COALESCE(
                        (SELECT COUNT(DISTINCT word)
                         FROM unnest(cw.word_array) AS word
                         WHERE word IN (SELECT target_word FROM relevant_translations)
                        ),
                        0
                    ) AS translation_count
                FROM cleaned_words cw
                GROUP BY cw.imports_id, cw.title, cw.url, cw.source_lang, cw.target_lang, cw.import_owner, cw.word_array;
            """;
        try {
            return template.query(sql, rowMapperStore.getDocumentWithTranslationDataMapper(),
                    owner,
                    targetLanguage.name(),
                    owner,
                    targetLanguage.name()
            );
        } catch (DataAccessException e) {
            log.error(String.valueOf(e.getCause()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
