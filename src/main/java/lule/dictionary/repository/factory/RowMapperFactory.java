package lule.dictionary.repository.factory;

import lule.dictionary.entity.application.implementation.imports.DictionaryImportWithId;
import lule.dictionary.entity.application.implementation.imports.base.DictionaryImport;
import lule.dictionary.entity.application.implementation.translation.base.DictionaryTranslation;
import lule.dictionary.entity.application.implementation.userProfile.base.DictionaryUserProfile;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.entity.application.interfaces.userProfile.base.UserProfile;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperFactory {
    public static final RowMapper<UserProfile> USER_PROFILE = ((rs, rowNum) ->
            DictionaryUserProfile.builder()
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .build()
    );
    public static final RowMapper<Translation> TRANSLATION = ((rs, rowNum) ->
            DictionaryTranslation.builder()
                    .sourceWord(rs.getString("source_word"))
                    .targetWord(rs.getString("target_word"))
                    .familiarity(Familiarity.valueOf(rs.getString("familiarity")))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .owner(rs.getString("translation_owner"))
                    .build()
    );

    public static final RowMapper<Integer> TRANSLATION_ID = ((rs, rowNum) -> rs.getInt("translations_id"));


    public static final RowMapper<ImportWithId> IMPORT_WITH_ID = ((rs, rowNum) ->
            DictionaryImportWithId.builder()
                    .title(rs.getString("title"))
                    .content(rs.getString("content"))
                    .url(rs.getString("url"))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .owner(rs.getString("import_owner"))
                    .id(rs.getInt("imports_id"))
                    .build()
    );
    public static final RowMapper<Import> IMPORT = ((rs, rowNum) ->
            DictionaryImport.builder()
                    .title(rs.getString("title"))
                    .content(rs.getString("content"))
                    .url(rs.getString("url"))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .owner(rs.getString("import_owner"))
                    .build()
    );
    public static final RowMapper<Integer> IMPORT_ID = ((rs, rowNum) -> rs.getInt("imports_id"));

}
