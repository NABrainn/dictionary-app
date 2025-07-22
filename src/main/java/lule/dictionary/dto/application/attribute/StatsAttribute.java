package lule.dictionary.dto.application.attribute;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record StatsAttribute(int wordsLearned,
                             int dailyStreak,
                             @NonNull String wordsLearnedText,
                             @NonNull String daysSingularText,
                             @NonNull String daysPluralText) {
}
