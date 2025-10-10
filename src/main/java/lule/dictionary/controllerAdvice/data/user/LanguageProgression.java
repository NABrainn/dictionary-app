package lule.dictionary.controllerAdvice.data.user;

public record LanguageProgression(int wordsLearned,
                                  int dailyStreak) {
    public static LanguageProgression of(int wordsLearned,
                                         int dailyStreak) {
        return new LanguageProgression(wordsLearned, dailyStreak);
    }
}
