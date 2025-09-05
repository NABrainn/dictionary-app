package lule.dictionary.translations.data.repository;

import lule.dictionary.language.service.Language;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.TranslationImp;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public record TranslationRowMapperStore(RowMapper<Translation> translationMapper,
                                        RowMapper<String> sourceWordsMapper,
                                        RowMapper<Integer> translationIdMapper) {
    public TranslationRowMapperStore {
        translationMapper = ((rs, rowNum) ->
                TranslationImp.builder()
                    .sourceWords(Arrays.asList((String[]) rs.getArray("source_words").getArray()))
                    .targetWord(rs.getString("target_word"))
                    .familiarity(Familiarity.valueOf(rs.getString("familiarity")))
                    .sourceLanguage(Language.valueOf(rs.getString("source_lang")))
                    .targetLanguage(Language.valueOf(rs.getString("target_lang")))
                    .owner(rs.getString("translation_owner"))
                    .isPhrase(rs.getBoolean("is_phrase"))
                    .build());
        sourceWordsMapper = ((rs, rowNum) -> rs.getString("word"));
        translationIdMapper = ((rs, rowNum) -> rs.getInt("translations_id"));
    }
}
