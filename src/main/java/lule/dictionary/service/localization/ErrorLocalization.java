package lule.dictionary.service.localization;

import lule.dictionary.service.language.Language;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ErrorLocalization {

    public static Map<String, String> documentForm(Language language) {
        List<String> keys = List.of();
        return switch (language) {
            case NO -> constructMap(keys, List.of("Leksjoner", "Vokabular", "Ord", "Dag", "Dager", "Logg på", "Logg av"));
            case IT -> constructMap(keys, List.of("Lezioni", "Vocabolario", "Parole", "Giorno", "Giorni", "Accedi", "Disconnettersi"));
            case PL -> constructMap(keys, List.of("Lekcje", "Słownictwo", "Słowa", "Dzień", "Dni", "Zaloguj się", "Wyloguj się"));
            default -> constructMap(keys, List.of("Lessons", "Vocabulary", "Words", "Day", "Days", "Log in", "Log out"));
        };
    }

    private static Map<String, String> constructMap(List<String> keys, List<String> values) {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Lists must have equal length: size of keys: " + keys.size() + "; size of values: " + values.size());
        }
        return IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }
}
