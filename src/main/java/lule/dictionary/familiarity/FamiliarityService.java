package lule.dictionary.familiarity;

import lule.dictionary.translations.data.Familiarity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class FamiliarityService {
    public int getFamiliarityAsDigit(Familiarity familiarity) {
        return switch (familiarity) {
            case UNKNOWN -> 1;
            case RECOGNIZED -> 2;
            case FAMILIAR -> 3;
            case KNOWN -> 4;
            case IGNORED -> 5;
        };
    }

    public Map<Integer, Familiarity> getFamiliarityTable() {
        return new TreeMap<>(Map.of(
                1, Familiarity.UNKNOWN,
                2, Familiarity.RECOGNIZED,
                3, Familiarity.FAMILIAR,
                4, Familiarity.KNOWN,
                5, Familiarity.IGNORED)
        );
    }

    public Familiarity getFamiliarityFromDigit(int digit) {
        return Familiarity.values()[digit];
    }
}
