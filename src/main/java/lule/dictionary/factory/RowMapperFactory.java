package lule.dictionary.factory;

import lule.dictionary.dto.application.interfaces.imports.Import;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.factory.dto.ImportFactory;
import lule.dictionary.factory.dto.TranslationFactory;
import lule.dictionary.factory.dto.UserProfileFactory;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperFactory {
    public static final RowMapper<UserProfile> USER_PROFILE = ((rs, rowNum) ->
            UserProfileFactory.createUserProfile(
                    UserProfileFactory.createCredentials(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password")
                    ),
                    UserProfileFactory.createSettings(
                            Language.valueOf(rs.getString("source_lang")),
                            Language.valueOf(rs.getString("target_lang"))
                    )
            )
    );
    public static final RowMapper<Translation> TRANSLATION = ((rs, rowNum) ->
            TranslationFactory.createTranslation(
                    TranslationFactory.createTranslationDetails(
                        rs.getString("source_word"),
                        rs.getString("target_word"),
                        Familiarity.valueOf(rs.getString("familiarity"))
                    ),
                    UserProfileFactory.createSettings(
                        Language.valueOf(rs.getString("source_lang")),
                        Language.valueOf(rs.getString("target_lang"))

                    ),
                    rs.getString("translation_owner")
            ));
    public static final RowMapper<Integer> TRANSLATION_ID = ((rs, rowNum) -> rs.getInt("translations_id"));


    public static final RowMapper<Import> IMPORT = ((rs, rowNum) ->
            ImportFactory.createImport(
                    ImportFactory.createImportDetails(
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("url")
                    ),
                    UserProfileFactory.createSettings(
                            Language.valueOf(rs.getString("source_lang")),
                            Language.valueOf(rs.getString("target_lang"))
                    ),
                    rs.getString("import_owner")
            )
    );
    public static final RowMapper<Integer> IMPORT_ID = ((rs, rowNum) -> rs.getInt("imports_id"));

}
