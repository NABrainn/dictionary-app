package lule.dictionary.translationFetching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lule.dictionary.language.service.Language;

import java.util.List;

@FunctionalInterface
public interface TranslationFetcher {
    List<String> translate(Language sourceLanguage, Language targetLanguage, String targetWord) throws JsonProcessingException;
}
