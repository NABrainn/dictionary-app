package lule.dictionary.service.language;

import java.util.Map;

public class LanguageHelper {

    public static Map<Language, String> languageMapNames = Map.of(
            Language.NO, "Norwegian",
            Language.PL, "Polish",
            Language.EN, "English",
            Language.IT, "Italian"
    );

    public static Map<Language, String> languageMapAbbreviations = Map.of(
            Language.PL, "pl",
            Language.EN, "en",
            Language.NO, "nb",
            Language.IT, "it"
    );
}
