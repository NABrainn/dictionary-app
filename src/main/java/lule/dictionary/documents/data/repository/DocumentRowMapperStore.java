package lule.dictionary.documents.data.repository;

import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.entity.DocumentImp;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.documents.data.entity.DocumentWithTranslationDataImp;
import lule.dictionary.language.service.Language;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public record DocumentRowMapperStore(RowMapper<Document> documentMapper,
                                     RowMapper<Integer> documentIdMapper,
                                     RowMapper<DocumentWithTranslationData> documentWithTranslationDataMapper) {
    public DocumentRowMapperStore {
        documentMapper = ((rs, rowNum) -> DocumentImp.builder()
                .title(rs.getString("title"))
                .url(rs.getString("url"))
                .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                .owner(rs.getString("import_owner"))
                .pageContent(rs.getString("page_content"))
                .totalContentLength(rs.getInt("total_length"))
                .build());
        documentIdMapper = ((rs, rowNum) -> rs.getInt("imports_id"));
        documentWithTranslationDataMapper = ((rs, rowNum) -> DocumentWithTranslationDataImp.builder()
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
                .build());
    }
}
