package lule.dictionary.translations.data.repository;

import lule.dictionary.language.service.Language;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.Translation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import lombok.Getter;

import java.util.Arrays;

@Component
@Getter
public class TranslationRowMapperStore {

    private final RowMapper<Translation> translationMapper;
    private final RowMapper<String> sourceWordsMapper;
    private final RowMapper<Integer> translationIdMapper;

    @Autowired
    public TranslationRowMapperStore() {
        translationMapper = (rs, rowNum) -> Translation.builder()
                .sourceWords(Arrays.asList((String[]) rs.getArray("source_words").getArray()))
                .targetWord(rs.getString("target_word"))
                .familiarity(Familiarity.valueOf(rs.getString("familiarity")))
                .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                .owner(rs.getString("translation_owner"))
                .isPhrase(rs.getBoolean("is_phrase"))
                .build();

        sourceWordsMapper = (rs, rowNum) -> rs.getString("word");

        translationIdMapper = (rs, rowNum) -> rs.getInt("translations_id");
    }
}