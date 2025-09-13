package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record UrlSubmissionStrategy(@NonNull String title,
                                    @NonNull String url,
                                    @NonNull String urlPlaceholderText) implements SubmissionStrategy, Validated {
    public static UrlSubmissionStrategy of(String title, String url, String urlPlaceholderText) {
        return new UrlSubmissionStrategy(title, url, urlPlaceholderText);
    }

    @Override
    public Set<Map<String, String>> validate(Language language) {
        Set<Map<String, String>> violations = new HashSet<>();
        if (title.isBlank()) {
            violations.add(Map.of(
                    "title",
                    switch (language) {
                        case PL -> "Tytuł nie może być pusty";
                        case EN -> "Title cannot be empty";
                        case IT -> "Il titolo non può essere vuoto";
                        case NO -> "Tittelen kan ikke være tom";
                    }
            ));
        }
        if (title.length() > 500) {
            violations.add(Map.of(
                    "title",
                    switch (language) {
                        case PL -> "Tytuł nie może przekraczać 500 znaków";
                        case EN -> "Title cannot exceed 500 characters";
                        case IT -> "Il titolo non può superare i 500 caratteri";
                        case NO -> "Tittelen kan ikke overstige 500 tegn";
                    }
            ));
        }
        if (url.isBlank()) {
            violations.add(Map.of(
                    "url",
                    switch (language) {
                        case PL -> "Adres URL nie może być pusty";
                        case EN -> "URL cannot be empty";
                        case IT -> "L'URL non può essere vuoto";
                        case NO -> "URL-en kan ikke være tom";
                    }
            ));
        }
        if (url.length() > 500) {
            violations.add(Map.of(
                    "url",
                    switch (language) {
                        case PL -> "Adres URL nie może przekraczać 500 znaków";
                        case EN -> "URL cannot exceed 500 characters";
                        case IT -> "L'URL non può superare i 500 caratteri";
                        case NO -> "URL-en kan ikke overstige 500 tegn";
                    }
            ));
        }
        return violations;
    }
}