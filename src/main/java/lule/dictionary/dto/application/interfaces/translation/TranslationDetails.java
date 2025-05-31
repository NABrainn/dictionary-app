package lule.dictionary.dto.application.interfaces.translation;

import lule.dictionary.enumeration.Familiarity;

public interface TranslationDetails {
    String sourceWord();
    String targetWord();
    Familiarity familiarity();
}
