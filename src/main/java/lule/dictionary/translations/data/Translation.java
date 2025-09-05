package lule.dictionary.translations.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

import java.util.List;

@Builder(toBuilder = true)
public record Translation(@NonNull List<String> sourceWords,
                          @NonNull String targetWord,
                          @NonNull Familiarity familiarity,
                          @NonNull Language sourceLanguage,
                          @NonNull Language targetLanguage,
                          @NonNull String owner,
                          boolean isPhrase) {
        @Override
        public boolean equals(Object object) {
                if(object == null) {
                        return false;
                }
                if(!(object instanceof Translation)) {
                        return false;
                }
                return this.targetWord.equals(((Translation) object).targetWord);
        }
}
