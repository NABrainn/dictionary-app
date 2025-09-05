package lule.dictionary.translations.data;

import lule.dictionary.enumeration.Familiarity;

import java.util.List;

public interface TranslationDetails {
    List<String> sourceWords();
    String targetWord();
    Familiarity familiarity();
    boolean isPhrase();
}
