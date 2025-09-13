package lule.dictionary.documents.data.documentSubmission;

import lombok.NonNull;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record ContentSubmissionStrategy(@NonNull String title,
                                        @NonNull String content,
                                        @NonNull String contentPlaceholderText) implements SubmissionStrategy, Validated {
    public static ContentSubmissionStrategy of(String title, String content, String contentPlaceholderText) {
        return new ContentSubmissionStrategy(title, content, contentPlaceholderText);
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
        if (content.isBlank()) {
            violations.add(Map.of(
                    "content",
                    switch (language) {
                        case PL -> "Treść nie może być pusta";
                        case EN -> "Content cannot be empty";
                        case IT -> "Il contenuto non può essere vuoto";
                        case NO -> "Innholdet kan ikke være tomt";
                    }
            ));
        }
        if (content.length() > 10000) {
            violations.add(Map.of(
                    "content",
                    switch (language) {
                        case PL -> "Treść nie może przekraczać 10000 znaków";
                        case EN -> "Content cannot exceed 10000 characters";
                        case IT -> "Il contenuto non può superare i 10000 caratteri";
                        case NO -> "Innholdet kan ikke overstige 10000 tegn";
                    }
            ));
        }
        return violations;
    }
}