package lule.dictionary.translations.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

import java.util.List;

@Builder
public record Translation(@NonNull List<String> sourceWords,
                          @NonNull String processedTargetWord,
                          @NonNull Familiarity familiarity,
                          @NonNull Language sourceLanguage,
                          @NonNull Language targetLanguage,
                          @NonNull String owner,
                          boolean isPhrase) {
        public Translation withSourceWords(List<String> sourceWords) {
                return Translation.builder()
                        .sourceWords(sourceWords)
                        .processedTargetWord(processedTargetWord)
                        .familiarity(familiarity)
                        .sourceLanguage(sourceLanguage)
                        .targetLanguage(targetLanguage)
                        .owner(owner)
                        .isPhrase(isPhrase)
                        .build();
        }
        public static Translation nonTranslation(@NonNull String processedTargetWord,
                                                 @NonNull Language sourceLanguage,
                                                 @NonNull Language targetLanguage,
                                                 @NonNull String owner) {
                return Translation.builder()
                        .sourceWords(List.of())
                        .processedTargetWord(processedTargetWord)
                        .familiarity(Familiarity.UNKNOWN)
                        .sourceLanguage(sourceLanguage)
                        .targetLanguage(targetLanguage)
                        .owner(owner)
                        .isPhrase(false)
                        .build();
        }
        @Override
        public boolean equals(Object object) {
                if(object == null) {
                        return false;
                }
                if(!(object instanceof Translation translation)) {
                        return false;
                }
                return this.processedTargetWord.equals(translation.processedTargetWord);
        }
}
