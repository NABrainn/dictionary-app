package lule.dictionary.validation.data.rule;

import lombok.NonNull;

public record Size(@NonNull String input,
                   int min,
                   int max) implements Rule {
    public static Size of(String input, int min, int max) {
        return new Size(input, min, max);
    }

    @Override
    public boolean isValid() {
        String trimmed = input.trim();
        if(trimmed.length() < min) {
            return false;
        }
        return trimmed.length() <= max;
    }
}
