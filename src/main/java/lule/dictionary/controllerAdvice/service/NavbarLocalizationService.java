package lule.dictionary.controllerAdvice.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.controllerAdvice.data.NavbarLocalizationKey;
import lule.dictionary.language.service.Language;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NavbarLocalizationService {

    public Map<NavbarLocalizationKey, String> get(Language language) {
        return switch (language) {
            case PL -> Map.ofEntries(
                    Map.entry(NavbarLocalizationKey.LESSONS, "Lekcje"),
                    Map.entry(NavbarLocalizationKey.VOCABULARY, "Słownictwo"),
                    Map.entry(NavbarLocalizationKey.WORDS, "Słowa"),
                    Map.entry(NavbarLocalizationKey.DAYS_SINGULAR, "Dzień"),
                    Map.entry(NavbarLocalizationKey.DAYS_PLURAL, "Dni"),
                    Map.entry(NavbarLocalizationKey.LOG_IN, "Zaloguj się"),
                    Map.entry(NavbarLocalizationKey.LOG_OUT, "Wyloguj się"),
                    Map.entry(NavbarLocalizationKey.SETTINGS, "Ustawienia"),
                    Map.entry(NavbarLocalizationKey.LANGUAGE, "Język"),
                    Map.entry(NavbarLocalizationKey.USER_INTERFACE, "Interfejs użytkownika"),
                    Map.entry(NavbarLocalizationKey.TRANSLATIONS, "Tłumaczenia")
            );
            case EN -> Map.ofEntries(
                    Map.entry(NavbarLocalizationKey.LESSONS, "Lessons"),
                    Map.entry(NavbarLocalizationKey.VOCABULARY, "Vocabulary"),
                    Map.entry(NavbarLocalizationKey.WORDS, "Words"),
                    Map.entry(NavbarLocalizationKey.DAYS_SINGULAR, "Day"),
                    Map.entry(NavbarLocalizationKey.DAYS_PLURAL, "Days"),
                    Map.entry(NavbarLocalizationKey.LOG_IN, "Log in"),
                    Map.entry(NavbarLocalizationKey.LOG_OUT, "Log out"),
                    Map.entry(NavbarLocalizationKey.SETTINGS, "Settings"),
                    Map.entry(NavbarLocalizationKey.LANGUAGE, "Language"),
                    Map.entry(NavbarLocalizationKey.USER_INTERFACE, "User Interface"),
                    Map.entry(NavbarLocalizationKey.TRANSLATIONS, "Translations")
            );
            case IT -> Map.ofEntries(
                    Map.entry(NavbarLocalizationKey.LESSONS, "Lezioni"),
                    Map.entry(NavbarLocalizationKey.VOCABULARY, "Vocabolario"),
                    Map.entry(NavbarLocalizationKey.WORDS, "Parole"),
                    Map.entry(NavbarLocalizationKey.DAYS_SINGULAR, "Giorno"),
                    Map.entry(NavbarLocalizationKey.DAYS_PLURAL, "Giorni"),
                    Map.entry(NavbarLocalizationKey.LOG_IN, "Accedi"),
                    Map.entry(NavbarLocalizationKey.LOG_OUT, "Esci"),
                    Map.entry(NavbarLocalizationKey.SETTINGS, "Impostazioni"),
                    Map.entry(NavbarLocalizationKey.LANGUAGE, "Lingua"),
                    Map.entry(NavbarLocalizationKey.USER_INTERFACE, "Interfaccia utente"),
                    Map.entry(NavbarLocalizationKey.TRANSLATIONS, "Traduzioni")
            );
            case NO -> Map.ofEntries(
                    Map.entry(NavbarLocalizationKey.LESSONS, "Leksjoner"),
                    Map.entry(NavbarLocalizationKey.VOCABULARY, "Ordforråd"),
                    Map.entry(NavbarLocalizationKey.WORDS, "Ord"),
                    Map.entry(NavbarLocalizationKey.DAYS_SINGULAR, "Dag"),
                    Map.entry(NavbarLocalizationKey.DAYS_PLURAL, "Dager"),
                    Map.entry(NavbarLocalizationKey.LOG_IN, "Logg inn"),
                    Map.entry(NavbarLocalizationKey.LOG_OUT, "Logg ut"),
                    Map.entry(NavbarLocalizationKey.SETTINGS, "Innstillinger"),
                    Map.entry(NavbarLocalizationKey.LANGUAGE, "Språk"),
                    Map.entry(NavbarLocalizationKey.USER_INTERFACE, "Brukergrensesnitt"),
                    Map.entry(NavbarLocalizationKey.TRANSLATIONS, "Oversettelser")
            );
        };
    }
}
