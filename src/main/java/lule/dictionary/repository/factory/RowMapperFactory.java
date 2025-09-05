package lule.dictionary.repository.factory;

import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.userProfiles.data.UserProfileImp;
import lule.dictionary.language.service.Language;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperFactory {
    public static final RowMapper<UserProfile> USER_PROFILE = ((rs, rowNum) ->
            UserProfileImp.builder()
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .userInterfaceLanguage(Language.valueOf(rs.getString("ui_lang")))
                    .wordsAddedToday(rs.getInt("words_added_today"))
                    .offset(rs.getString("tz_offset"))
                    .dailyStreak(rs.getInt("day_count"))
                    .build());
}
