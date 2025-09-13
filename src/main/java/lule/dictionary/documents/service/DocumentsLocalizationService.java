package lule.dictionary.documents.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.language.service.Language;
import org.springframework.stereotype.Service;

import java.util.Map;

import static lule.dictionary.documents.data.DocumentLocalizationKey.*;

@Service
@RequiredArgsConstructor
public class DocumentsLocalizationService {
    public Map<DocumentLocalizationKey, String> get(Language language) {
        return switch (language) {
            case PL -> Map.ofEntries(
                    Map.entry(AUTHOR, "Autor"),
                    Map.entry(WORDS_TOTAL, "Całkowita liczba słów"),
                    Map.entry(NEW_WORDS, "Nowe słowa"),
                    Map.entry(TRANSLATIONS, "Tłumaczenia"),
                    Map.entry(ADD_BOOK, "Dodaj książkę"),
                    Map.entry(THERE_ARE_NO_DOCUMENTS_IN_THE_LIBRARY, "W bibliotece nie ma dokumentów"),
                    Map.entry(CLICK_HERE, "Kliknij tutaj"),
                    Map.entry(TO_ADD_YOUR_FIRST, "aby dodać swój pierwszy"),
                    Map.entry(TITLE, "Tytuł"),
                    Map.entry(CONTENT, "Treść"),
                    Map.entry(IMPORT_BY_URL, "Importuj przez URL"),
                    Map.entry(INSERT_MANUALLY, "Wstaw ręcznie"),
                    Map.entry(SUBMIT, "Wyślij"),
                    Map.entry(SPACE_FOR_URL, "Wstaw adres URL"),
                    Map.entry(SPACE_FOR_CONTENT, "Wstaw treść")
            );
            case EN -> Map.ofEntries(
                    Map.entry(AUTHOR, "Author"),
                    Map.entry(WORDS_TOTAL, "Total words"),
                    Map.entry(NEW_WORDS, "New words"),
                    Map.entry(TRANSLATIONS, "Translations"),
                    Map.entry(ADD_BOOK, "Add book"),
                    Map.entry(THERE_ARE_NO_DOCUMENTS_IN_THE_LIBRARY, "There are no documents in the library"),
                    Map.entry(CLICK_HERE, "Click here"),
                    Map.entry(TO_ADD_YOUR_FIRST, "to add your first"),
                    Map.entry(TITLE, "Title"),
                    Map.entry(CONTENT, "Content"),
                    Map.entry(IMPORT_BY_URL, "Import by URL"),
                    Map.entry(INSERT_MANUALLY, "Insert manually"),
                    Map.entry(SUBMIT, "Submit"),
                    Map.entry(SPACE_FOR_URL, "Enter URL here"),
                    Map.entry(SPACE_FOR_CONTENT, "Enter content here")
            );
            case IT -> Map.ofEntries(
                    Map.entry(AUTHOR, "Autore"),
                    Map.entry(WORDS_TOTAL, "Parole totali"),
                    Map.entry(NEW_WORDS, "Nuove parole"),
                    Map.entry(TRANSLATIONS, "Traduzioni"),
                    Map.entry(ADD_BOOK, "Aggiungi libro"),
                    Map.entry(THERE_ARE_NO_DOCUMENTS_IN_THE_LIBRARY, "Non ci sono documenti nella libreria"),
                    Map.entry(CLICK_HERE, "Clicca qui"),
                    Map.entry(TO_ADD_YOUR_FIRST, "per aggiungere il tuo primo"),
                    Map.entry(TITLE, "Titolo"),
                    Map.entry(CONTENT, "Contenuto"),
                    Map.entry(IMPORT_BY_URL, "Importa tramite URL"),
                    Map.entry(INSERT_MANUALLY, "Inserisci manualmente"),
                    Map.entry(SUBMIT, "Invia"),
                    Map.entry(SPACE_FOR_URL, "Inserisci l'URL qui"),
                    Map.entry(SPACE_FOR_CONTENT, "Inserisci il contenuto qui")
            );
            case NO -> Map.ofEntries(
                    Map.entry(AUTHOR, "Forfatter"),
                    Map.entry(WORDS_TOTAL, "Totalt antall ord"),
                    Map.entry(NEW_WORDS, "Nye ord"),
                    Map.entry(TRANSLATIONS, "Oversettelser"),
                    Map.entry(ADD_BOOK, "Legg til bok"),
                    Map.entry(THERE_ARE_NO_DOCUMENTS_IN_THE_LIBRARY, "Det er ingen dokumenter i biblioteket"),
                    Map.entry(CLICK_HERE, "Klikk her"),
                    Map.entry(TO_ADD_YOUR_FIRST, "for å legge til din første"),
                    Map.entry(TITLE, "Tittel"),
                    Map.entry(CONTENT, "Innhold"),
                    Map.entry(IMPORT_BY_URL, "Importer via URL"),
                    Map.entry(INSERT_MANUALLY, "Sett inn manuelt"),
                    Map.entry(SUBMIT, "Send"),
                    Map.entry(SPACE_FOR_URL, "Skriv inn URL her"),
                    Map.entry(SPACE_FOR_CONTENT, "Skriv inn innhold her")
            );
        };
    }
}
