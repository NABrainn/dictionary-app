package lule.dictionary.jsoup.service.exception;

import lombok.Getter;
import lule.dictionary.language.service.Language;

import java.util.Map;

@Getter
public class InvalidUriException extends RuntimeException {

    private Map<Language, String> localizedMessages;

    public InvalidUriException(String message) {
        super(message);
        initializeMap();
    }

    public InvalidUriException(String message, Language language) {
        super(message);
        initializeMap();
        this.localizedMessages.get(language);
    }

    private void initializeMap() {
        this.localizedMessages = Map.of(
                Language.EN, "Invalid or empty URI",
                Language.PL, "Nieprawidłowy lub pusty URI",
                Language.IT, "URI non valido o vuoto",
                Language.NO, "Ugyldig eller tom URI"
        );
    }
}
