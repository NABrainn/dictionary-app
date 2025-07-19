package lule.dictionary.service.localization;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.language.Language;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LocalizationServiceImp implements LocalizationService {

    @Override
    public Map<String, String> navbarLocalization(Language language) {
        List<String> keys = List.of("home", "words", "days_singular", "days_plural", "log_in", "log_out");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Hjem", "Ord", "Dag", "Dager", "Logg på" , "Logg av"));
            case IT -> constructMap(keys, List.of("Casa", "Parole", "Giorno", "Giorni", "Login", "Disconnettersi"));
            case PL -> constructMap(keys, List.of("Dom", "Słowa", "Dzień", "Dni", "Zaloguj się", "Wyloguj się"));
            default -> constructMap(keys, List.of("Home", "Words", "Day", "Days", "Log in", "Log out"));
        };
    }

    @Override
    public Map<String, String> documentListLocalization(Language language) {
        List<String> keys = List.of("created_by", "add_book", "there_are_no_documents_in_the_library", "click_here", "to_add_your_first");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Oprettet av", "Ny dokument", "Det finnes ingen dokumentar i biblioteket ditt.", "Klikk her", "for å legge til din første"));
            case IT -> constructMap(keys, List.of("Creato da", "Nuovo documento", "Non ci sono libri in biblioteca.", "Clicca qui", "per aggiungere il tuo primo documento"));
            case PL -> constructMap(keys, List.of("Autor", "Nowy dokument", "Nie znaleziono dokumentów w bibliotece.", "Kliknij tutaj", "aby dodać swój pierwszy"));
            default -> constructMap(keys, List.of("Created by", "New document", "There are no documents in the library.", "Click here", "to add your first"));
        };
    }

    @Override
    public Map<String, String> authLocalization(Language language) {
        List<String> keys = List.of("log_in", "sign_up", "username", "email", "password", "create_account", "submit");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Logg på", "Registrer", "Brukernavn", "Email", "Passord", "Opprett konto", "Send inn"));
            case IT -> constructMap(keys, List.of("Create da", "Nuovo documento", "Iscrizione", "Email", "Password", "Creare un account", "invia"));
            case PL -> constructMap(keys, List.of("Zaloguj się", "Zarejestruj się", "Nazwa użytkownika", "Email", "Hasło", "Załóż konto", "Wyślij"));
            default -> constructMap(keys, List.of("Log in", "Sign up", "Username", "Email", "Password", "Create account", "Submit"));
        };
    }

    private Map<String, String> constructMap(List<String> keys, List<String> values) {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Lists must have equal length");
        }
        return IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }
}
