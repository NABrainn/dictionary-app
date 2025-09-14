package lule.dictionary.translations.data.request;

public record ConfigureFlashcardRequest(int familiarity,
                                        int quantity,
                                        boolean isPhrase) {
    public static ConfigureFlashcardRequest of(int familiarity, int quantity, boolean isPhrase) {
        return new ConfigureFlashcardRequest(familiarity, quantity, isPhrase);
    }
}
