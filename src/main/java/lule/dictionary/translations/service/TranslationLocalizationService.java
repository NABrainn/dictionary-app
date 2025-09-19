package lule.dictionary.translations.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import lule.dictionary.userProfiles.data.UserProfile;
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

    public Map<TranslationLocalizationKey, String> vocabularyMessages(@NonNull Language language) {
        return switch (language) {
            case PL -> Map.ofEntries(
                    Map.entry(TranslationLocalizationKey.FAMILIARITY, "Nieznany"),
                    Map.entry(TranslationLocalizationKey.HOW_MANY, "Ile"),
                    Map.entry(TranslationLocalizationKey.PHRASES, "Frazy"),
                    Map.entry(TranslationLocalizationKey.WORDS, "Słowa"),
                    Map.entry(TranslationLocalizationKey.REVIEW_TRANSLATIONS, "Przejrzyj tłumaczenia")
            );
            case EN -> Map.ofEntries(
                    Map.entry(TranslationLocalizationKey.FAMILIARITY, "Familiarity"),
                    Map.entry(TranslationLocalizationKey.HOW_MANY, "How many"),
                    Map.entry(TranslationLocalizationKey.PHRASES, "Phrases"),
                    Map.entry(TranslationLocalizationKey.WORDS, "Words"),
                    Map.entry(TranslationLocalizationKey.REVIEW_TRANSLATIONS, "Review translations")
            );
            case IT -> Map.ofEntries(
                    Map.entry(TranslationLocalizationKey.FAMILIARITY, "Familiarità"),
                    Map.entry(TranslationLocalizationKey.HOW_MANY, "Quanti"),
                    Map.entry(TranslationLocalizationKey.PHRASES, "Frasi"),
                    Map.entry(TranslationLocalizationKey.WORDS, "Parole"),
                    Map.entry(TranslationLocalizationKey.REVIEW_TRANSLATIONS, "Rivedi traduzioni")
            );
            case NO -> Map.ofEntries(
                    Map.entry(TranslationLocalizationKey.FAMILIARITY, "Kjennskap"),
                    Map.entry(TranslationLocalizationKey.HOW_MANY, "Hvor mange"),
                    Map.entry(TranslationLocalizationKey.PHRASES, "Fraser"),
                    Map.entry(TranslationLocalizationKey.WORDS, "Ord"),
                    Map.entry(TranslationLocalizationKey.REVIEW_TRANSLATIONS, "Gjennomgå oversettelser")
            );
        };
    }
}
