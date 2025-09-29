package lule.dictionary.validation.data.rule;

public record NotEmpty(String input) implements Rule {
    public static NotEmpty of(String input) {
        return new NotEmpty(input);
    }

    @Override
    public boolean isValid() {
        if(input == null) {
            return false;
        }
        return !input.trim().isBlank();
    }
}
