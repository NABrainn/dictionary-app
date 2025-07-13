package lule.dictionary.dto.database.interfaces.translation;

import lule.dictionary.enumeration.Familiarity;

import java.util.List;

public interface TranslationDetails {
    List<String> sourceWords();
    String targetWord();
    Familiarity familiarity();
}
