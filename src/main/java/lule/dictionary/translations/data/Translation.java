package lule.dictionary.translations.data;

import lule.dictionary.language.service.Language;

import java.util.List;

public interface Translation {
    String owner();
    List<String> sourceWords();
    String targetWord();
    Familiarity familiarity();
    boolean isPhrase();
    Language sourceLanguage();
    Language targetLanguage();
}
