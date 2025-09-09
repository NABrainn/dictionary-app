package lule.dictionary.userProfiles.data.repository;

import lombok.Getter;
import lule.dictionary.language.service.Language;
import lule.dictionary.userProfiles.data.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserProfileRowMapperStore {

    private final RowMapper<UserProfile> userProfileMapper;

    @Autowired
    public UserProfileRowMapperStore() {
        userProfileMapper = ((rs, rowNum) ->
                UserProfile.builder()
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
}
