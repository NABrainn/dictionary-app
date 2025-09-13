package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public record UpdateSourceWordsRequest(@NonNull List<String> sourceWords,
                                       @NonNull String targetWord,
                                       @NonNull Familiarity familiarity,
                                       @NonNull Language sourceLanguage,
                                       @NonNull Language targetLanguage,
                                       @NonNull Language uiLanguage,
                                       @NonNull String owner,
                                       int selectedWordId,
                                       boolean isPhrase) implements TranslationsRequest, Validated {

    @Override
    public List<String> sourceWords() {
        return List.copyOf(sourceWords);
    }

    @Override
    public Set<Map<String, String>> validate(Language language) {
        Set<Map<String, String>> violations = new HashSet<>();
        if (sourceWords.isEmpty()) {
            violations.add(Map.of(
                    "sourceWords",
                    switch (language) {
                        case PL -> "Słowo źródłowe nie może być puste";
                        case EN -> "Source word cannot be empty";
                        case IT -> "La parola sorgente non può essere vuota";
                        case NO -> "Kildeordet kan ikke være tomt";
                    }
            ));
        }
        if(sourceWords.stream().anyMatch(word -> word.length() > 200)) {
            violations.add(Map.of(
                    "sourceWords",
                    switch (language) {
                        case PL -> "Słowo źródłowe jest za długie";
                        case EN -> "Source word is too long";
                        case IT -> "La parola sorgente è troppo lunga";
                        case NO -> "Kildeordet er for langt";
                    }
            ));
        }
        if(sourceWords.stream().anyMatch(String::isBlank)) {
            violations.add(Map.of(
                    "sourceWords",
                    switch (language) {
                        case PL -> "Słowo źródłowe nie może być puste";
                        case EN -> "Source word cannot be empty";
                        case IT -> "La parola sorgente non può essere vuota";
                        case NO -> "Kildeordet kan ikke være tomt";
                    }
            ));
        }

        if (targetWord.isBlank()) {
            violations.add(Map.of(
                    "targetWord",
                    switch (language) {
                        case PL -> "Słowo docelowe nie może być puste";
                        case EN -> "Target word cannot be empty";
                        case IT -> "La parola di destinazione non può essere vuota";
                        case NO -> "Målordet kan ikke være tomt";
                    }));
        }
        if (targetWord.length() > 200) {
            violations.add(Map.of(
                    "targetWord",
                    switch (language) {
                        case PL -> "Słowo docelowe jest za długie";
                        case EN -> "Target word is too long";
                        case IT -> "La parola di destinazione è troppo lunga";
                        case NO -> "Målordet er for langt";
                    }));
        }
        return violations;
    }
}
