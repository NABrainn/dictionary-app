package lule.dictionary.translations.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import org.springframework.stereotype.Service;

import java.util.Map;

import static lule.dictionary.translations.data.TranslationLocalizationKey.*;


@Service
@RequiredArgsConstructor
public class FlashcardLocalizationService {
    public Map<TranslationLocalizationKey, String> get(Language language) {
        return switch (language) {
            case PL -> Map.ofEntries(
                    Map.entry(FAMILIARITY, "Znajomość"),
                    Map.entry(HOW_MANY, "Ile"),
                    Map.entry(PHRASES, "Frazy"),
                    Map.entry(WORDS, "Słowa"),
                    Map.entry(REVIEW_TRANSLATIONS, "Przejrzyj tłumaczenia")
            );
            case EN -> Map.ofEntries(
                    Map.entry(FAMILIARITY, "Familiarity"),
                    Map.entry(HOW_MANY, "How many"),
                    Map.entry(PHRASES, "Phrases"),
                    Map.entry(WORDS, "Words"),
                    Map.entry(REVIEW_TRANSLATIONS, "Review translations")
            );
            case IT -> Map.ofEntries(
                    Map.entry(FAMILIARITY, "Familiarità"),
                    Map.entry(HOW_MANY, "Quanti"),
                    Map.entry(PHRASES, "Frasi"),
                    Map.entry(WORDS, "Parole"),
                    Map.entry(REVIEW_TRANSLATIONS, "Rivedi traduzioni")
            );
            case NO -> Map.ofEntries(
                    Map.entry(FAMILIARITY, "Kjennskap"),
                    Map.entry(HOW_MANY, "Hvor mange"),
                    Map.entry(PHRASES, "Fraser"),
                    Map.entry(WORDS, "Ord"),
                    Map.entry(REVIEW_TRANSLATIONS, "Gjennomgå oversettelser")
            );
        };
    }
}
