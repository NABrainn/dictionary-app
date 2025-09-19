package lule.dictionary.translations.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import org.springframework.stereotype.Service;

import java.util.Map;

import static lule.dictionary.translations.data.TranslationLocalizationKey.*;

@Service
@RequiredArgsConstructor
public class TranslationLocalizationService {
    public Map<TranslationLocalizationKey, String> translationFormMessages(Language language) {
        return switch (language) {
            case PL -> Map.ofEntries(
                    Map.entry(UNKNOWN, "Nieznany"),
                    Map.entry(RECOGNIZED, "Rozpoznany"),
                    Map.entry(FAMILIAR, "Znajomy"),
                    Map.entry(KNOWN, "Znany"),
                    Map.entry(IGNORED, "Zignorowany")
            );
            case EN -> Map.ofEntries(
                    Map.entry(UNKNOWN, "Unknown"),
                    Map.entry(RECOGNIZED, "Recognized"),
                    Map.entry(FAMILIAR, "Familiar"),
                    Map.entry(KNOWN, "Known"),
                    Map.entry(IGNORED, "Ignored")
            );
            case IT -> Map.ofEntries(
                    Map.entry(UNKNOWN, "Sconosciuto"),
                    Map.entry(RECOGNIZED, "Riconosciuto"),
                    Map.entry(FAMILIAR, "Familiare"),
                    Map.entry(KNOWN, "Noto"),
                    Map.entry(IGNORED, "Ignorato")
            );
            case NO -> Map.ofEntries(
                    Map.entry(UNKNOWN, "Ukjent"),
                    Map.entry(RECOGNIZED, "Gjenkjent"),
                    Map.entry(FAMILIAR, "Familier"),
                    Map.entry(KNOWN, "Kjent"),
                    Map.entry(IGNORED, "Ignorert")
            );
        };
    }
}
