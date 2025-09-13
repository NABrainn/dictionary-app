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
    public Map<TranslationLocalizationKey, String> get(Language language) {
        return switch (language) {
            case PL -> Map.ofEntries(
                    Map.entry(ADD_TRANSLATION, "Dodaj tłumaczenie"),
                    Map.entry(AI_TRANSLATION, "Tłumaczenie AI"),
                    Map.entry(UNKNOWN, "Nieznany"),
                    Map.entry(RECOGNIZED, "Rozpoznany"),
                    Map.entry(FAMILIAR, "Znajomy"),
                    Map.entry(KNOWN, "Znany"),
                    Map.entry(IGNORED, "Zignorowany"),
                    Map.entry(FAMILIARITY, "Znajomość"),
                    Map.entry(HOW_MANY, "Ile"),
                    Map.entry(PHRASES, "Frazy"),
                    Map.entry(WORDS, "Słowa"),
                    Map.entry(REVIEW_TRANSLATIONS, "Przejrzyj tłumaczenia")
            );
            case EN -> Map.ofEntries(
                    Map.entry(ADD_TRANSLATION, "Add translation"),
                    Map.entry(AI_TRANSLATION, "Ask AI"),
                    Map.entry(UNKNOWN, "Unknown"),
                    Map.entry(RECOGNIZED, "Recognized"),
                    Map.entry(FAMILIAR, "Familiar"),
                    Map.entry(KNOWN, "Known"),
                    Map.entry(IGNORED, "Ignored"),
                    Map.entry(FAMILIARITY, "Familiarity"),
                    Map.entry(HOW_MANY, "How many"),
                    Map.entry(PHRASES, "Phrases"),
                    Map.entry(WORDS, "Words"),
                    Map.entry(REVIEW_TRANSLATIONS, "Review translations")
            );
            case IT -> Map.ofEntries(
                    Map.entry(ADD_TRANSLATION, "Aggiungi traduzione"),
                    Map.entry(AI_TRANSLATION, "Traduzione AI"),
                    Map.entry(UNKNOWN, "Sconosciuto"),
                    Map.entry(RECOGNIZED, "Riconosciuto"),
                    Map.entry(FAMILIAR, "Familiare"),
                    Map.entry(KNOWN, "Noto"),
                    Map.entry(IGNORED, "Ignorato"),
                    Map.entry(FAMILIARITY, "Familiarità"),
                    Map.entry(HOW_MANY, "Quanti"),
                    Map.entry(PHRASES, "Frasi"),
                    Map.entry(WORDS, "Parole"),
                    Map.entry(REVIEW_TRANSLATIONS, "Rivedi traduzioni")
            );
            case NO -> Map.ofEntries(
                    Map.entry(ADD_TRANSLATION, "Legg til oversettelse"),
                    Map.entry(AI_TRANSLATION, "AI-oversettelse"),
                    Map.entry(UNKNOWN, "Ukjent"),
                    Map.entry(RECOGNIZED, "Gjenkjent"),
                    Map.entry(FAMILIAR, "Familier"),
                    Map.entry(KNOWN, "Kjent"),
                    Map.entry(IGNORED, "Ignorert"),
                    Map.entry(FAMILIARITY, "Kjennskap"),
                    Map.entry(HOW_MANY, "Hvor mange"),
                    Map.entry(PHRASES, "Fraser"),
                    Map.entry(WORDS, "Ord"),
                    Map.entry(REVIEW_TRANSLATIONS, "Gjennomgå oversettelser")
            );
        };
    }
}
