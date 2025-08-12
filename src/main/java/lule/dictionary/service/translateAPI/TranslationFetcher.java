package lule.dictionary.service.translateAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import lule.dictionary.service.language.Language;

import java.util.List;

@FunctionalInterface
public interface TranslationFetcher {
    List<String> translate(Language sourceLanguage, Language targetLanguage, String targetWord) throws JsonProcessingException;
}
