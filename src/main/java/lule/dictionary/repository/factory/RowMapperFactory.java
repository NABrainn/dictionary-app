package lule.dictionary.repository.factory;

import lule.dictionary.dto.database.implementation.imports.ImportWithTranslationDataImp;
import lule.dictionary.dto.database.implementation.imports.base.ImportImp;
import lule.dictionary.dto.database.implementation.imports.ImportWithPaginationImp;
import lule.dictionary.dto.database.implementation.translation.base.TranslationImp;
import lule.dictionary.dto.database.implementation.userProfile.base.UserProfileImp;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.dto.database.interfaces.imports.base.Document;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.dto.database.interfaces.userProfile.base.UserProfile;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.language.Language;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;

public class RowMapperFactory {
    public static final RowMapper<UserProfile> USER_PROFILE = ((rs, rowNum) ->
            UserProfileImp.builder()
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .wordsAddedToday(rs.getInt("words_added_today"))
                    .offset(rs.getString("tz_offset"))
                    .dailyStreak(rs.getInt("day_count"))
                    .build());
    public static final RowMapper<Translation> TRANSLATION = ((rs, rowNum) ->
            TranslationImp.builder()
                    .sourceWords(Arrays.asList((String[]) rs.getArray("source_words").getArray()))
                    .targetWord(rs.getString("target_word"))
                    .familiarity(Familiarity.valueOf(rs.getString("familiarity")))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .owner(rs.getString("translation_owner"))
                    .isPhrase(rs.getBoolean("is_phrase"))
                    .build());
    public static final RowMapper<Integer> TRANSLATION_ID = ((rs, rowNum) -> rs.getInt("translations_id"));
    public static final RowMapper<String> SOURCE_WORDS = ((rs, rowNum) -> rs.getString("word"));
    public static final RowMapper<Translation> PHRASES = ((rs, rowNum) ->
            TranslationImp.builder()
                .sourceWords(Arrays.asList((String[]) rs.getArray("source_words").getArray()))
                .targetWord(rs.getString("target_word"))
                .familiarity(Familiarity.valueOf(rs.getString("familiarity")))
                .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                .owner(rs.getString("translation_owner"))
                .isPhrase(rs.getBoolean("is_phrase"))
                .build());
    public static final RowMapper<ImportWithTranslationData> IMPORT_WITH_TRANSLATION_DATA = ((rs, rowNum) ->
            ImportWithTranslationDataImp.builder()
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
    public static final RowMapper<Document> IMPORT = ((rs, rowNum) ->
            ImportImp.builder()
                    .title(rs.getString("title"))
                    .url(rs.getString("url"))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .owner(rs.getString("import_owner"))
                    .pageContent(rs.getString("page_content"))
                    .totalContentLength(rs.getInt("total_length"))
                    .build()
    );
    public static final RowMapper<Integer> IMPORT_ID = ((rs, rowNum) -> rs.getInt("imports_id"));
}
