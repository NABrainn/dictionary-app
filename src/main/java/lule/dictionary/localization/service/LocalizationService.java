package lule.dictionary.localization.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LocalizationService {

    public Map<String, String> documentListLocalization(Language language) {
        List<String> keys = List.of("author", "words_total", "new_words", "translations", "add_book", "there_are_no_documents_in_the_library", "click_here", "to_add_your_first");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Oprettet av", "Totalt antall ord", "Nye ord", "Oversettelser", "Legg til bok", "Det finnes ingen dokumenter i biblioteket ditt.", "Klikk her", "for å legge til din første"));
            case IT -> constructMap(keys, List.of("Creato da", "Totale parole", "Nuove parole", "Traduzioni", "Aggiungi libro", "Non ci sono documenti nella biblioteca.", "Clicca qui", "per aggiungere il tuo primo"));
            case PL -> constructMap(keys, List.of("Autor", "Całkowita liczba słów", "Nowe słowa", "Tłumaczenia", "Dodaj książkę", "Nie znaleziono dokumentów w bibliotece.", "Kliknij tutaj", "aby dodać swój pierwszy"));
            default -> constructMap(keys, List.of("Author", "Words total", "New words", "Translations", "Add book", "There are no documents in the library.", "Click here", "to add your first"));
        };
    }

    public Map<String, String> documentFormLocalization(Language language) {
        List<String> keys = List.of("title", "content", "import_by_url", "insert_manually", "space_for_url", "space_for_content", "submit");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Tittel", "Innhold", "Importer via URL", "Sett inn manuelt", "Plass for URL", "Plass for innhold", "Send inn"));
            case IT -> constructMap(keys, List.of("Titolo", "Contenuto", "Importa tramite URL", "Inserisci manualmente", "Spazio per URL", "Spazio per contenuto", "Invia"));
            case PL -> constructMap(keys, List.of("Tytuł", "Treść", "Importuj przez URL", "Wstaw ręcznie", "Miejsce na URL", "Miejsce na treść", "Prześlij"));
            default -> constructMap(keys, List.of("Title", "Content", "Import by URL", "Insert manually", "Space for URL", "Space for content", "Submit"));
        };
    }

    public Map<String, String> translationFormLocalization(Language language) {
        List<String> keys = List.of("add_translation", "ai_translation", "unknown", "recognized", "familiar", "known", "ignored");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Legg til oversettelse", "AI-oversettelse", "Ukjent", "Gjenkjent", "Familier", "Kjent", "Ignorert"));
            case IT -> constructMap(keys, List.of("Aggiungi traduzione", "Traduzione AI", "Sconosciuto", "Riconosciuto", "Familiare", "Noto", "Ignorato"));
            case PL -> constructMap(keys, List.of("Dodaj tłumaczenie", "Tłumaczenie AI", "Nieznany", "Rozpoznany", "Znajomy", "Znany", "Zignorowany"));
            default -> constructMap(keys, List.of("Add translation", "Ask AI", "Unknown", "Recognized", "Familiar", "Known", "Ignored"));
        };
    }

    public Map<String, String> flashcardConfigLocalization(Language language) {
        List<String> keys = List.of("review_translations", "familiarity", "how_many", "translationMapper", "words");
        return switch (language) {
            case NO -> constructMap(keys, List.of("Se gjennom oversettelser", "Kjennskap", "Hvor mange", "Fraser", "Ord"));
            case IT -> constructMap(keys, List.of("Rivedi traduzioni", "Familiarità", "Quanti", "Frasi", "Parole"));
            case PL -> constructMap(keys, List.of("Przejrzyj tłumaczenia", "Znajomość", "Ile", "Frazy", "Słowa"));
            default -> constructMap(keys, List.of("Review translations", "Familiarity", "How many", "Phrases", "Words"));
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