package lule.dictionary.documents.data.repository;

import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.language.service.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import lombok.Getter;

@Component
@Getter
public class DocumentRowMapperStore {

    private final RowMapper<Document> documentMapper;
    private final RowMapper<Integer> documentIdMapper;
    private final RowMapper<DocumentWithTranslationData> documentWithTranslationDataMapper;

    @Autowired
    public DocumentRowMapperStore() {
        documentMapper = (rs, rowNum) -> Document.builder()
                .title(rs.getString("title"))
                .url(rs.getString("url"))
                .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                .owner(rs.getString("import_owner"))
                .pageContent(rs.getString("page_content"))
                .totalContentLength(rs.getInt("total_length"))
                .id(-1)
                .build();

        documentIdMapper = (rs, rowNum) -> rs.getInt("imports_id");

        documentWithTranslationDataMapper = (rs, rowNum) -> DocumentWithTranslationData.builder()
                .title(rs.getString("title"))
                .url(rs.getString("url"))
                .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                .owner(rs.getString("import_owner"))
                .totalContentLength(0)
                .id(rs.getInt("imports_id"))
                .wordCount(rs.getInt("word_count"))
                .newWordCount(rs.getInt("new_word_count"))
                .translationCount(rs.getInt("translation_count"))
                .build();
    }
}