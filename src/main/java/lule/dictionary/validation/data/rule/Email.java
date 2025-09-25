package lule.dictionary.validation.data.rule;

import lombok.NonNull;

import java.util.regex.Pattern;

public record Email(@NonNull String input) implements Rule {
    public static Email of(String email) {
        return new Email(email);
    }

    @Override
    public boolean isValid() {
        return Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                .matcher(input)
                .matches();
    }
}
