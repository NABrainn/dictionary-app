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
        List<String> keys = List.of("author", "words_total", "new_words", "translations", "add_book", "there_are_no_documents_in_the_library", "click_here", "to_add_your_first");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Oprettet av", "Ny dokument", "Det finnes ingen dokumentar i biblioteket ditt.", "Klikk her", "for å legge til din første"));
            case IT -> constructMap(keys, List.of("Creato da", "Nuovo documento", "Non ci sono libri in biblioteca.", "Clicca qui", "per aggiungere il tuo primo documento"));
            case PL -> constructMap(keys, List.of("Autor", "Nowy dokument", "Nie znaleziono dokumentów w bibliotece.", "Kliknij tutaj", "aby dodać swój pierwszy"));
            default -> constructMap(keys, List.of("Author", "Words total", "New words", "Translations", "New document", "There are no documents in the library.", "Click here", "to add your first"));
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

    @Override
    public Map<String, String> documentFormLocalization(Language language) {
        List<String> keys = List.of("title", "content", "import_by_url", "insert_manually", "space_for_url", "space_for_content", "submit");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Tittel", "Innhold", "Importer via URL", "Sett inn manuelt", "Plass for URL", "Plass for innhold", "Send inn"));
            case IT -> constructMap(keys, List.of("Accedi", "Contenuto", "Importa tramite URL", "Inserisci manualmente", "Spazio per URL", "Spazio per contenuto", "Invia"));
            case PL -> constructMap(keys, List.of("Tytuł", "Treść", "Importuj przez URL", "Wstaw ręcznie", "Miejsce na URL", "Miejsce na treść", "Prześlij"));
            default -> constructMap(keys, List.of("Title", "Content", "Import by URL", "Insert manually", "Space for URL", "Space for content", "Submit"));
        };
    }

    @Override
    public Map<String, String> translationFormLocalization(Language language) {
        List<String> keys = List.of("add_translation", "ai_translation", "unknown", "recognized", "familiar", "known", "ignored");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Legg til oversettelse", "AI-oversettelse", "Ukjent", "Gjenkjent", "Kjent", "Kjent", "Ignorert"));
            case IT -> constructMap(keys, List.of("Aggiungi traduzione", "Traduzione AI", "Sconosciuto", "Riconosciuto", "Familiare", "Noto", "Ignorato"));
            case PL -> constructMap(keys, List.of("Dodaj tłumaczenie", "Tłumaczenie AI", "Nieznany", "Rozpoznany", "Znajomy", "Znany", "Zignorowany"));
            default -> constructMap(keys, List.of("Add translation", "AI translation", "Unknown", "Recognized", "Familiar", "Known", "Ignored"));
        };
    }

    private Map<String, String> constructMap(List<String> keys, List<String> values) {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Lists must have equal length: size of keys: " + keys.size() + "; size of values: " + values.size());
        }
        return IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }
}
