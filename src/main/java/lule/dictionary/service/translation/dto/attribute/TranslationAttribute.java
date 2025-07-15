package lule.dictionary.service.translation.dto.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.imports.importService.dto.FormPositionData;

import java.util.Collections;
import java.util.Map;

@Builder
public record TranslationAttribute(int importId,
                                   int selectedWordId,
                                   int translationId,
                                   Translation translation,
                                   int currentFamiliarity,
                                   @NonNull Map<Integer, Familiarity> familiarityLevels,
                                   int page,
                                   FormPositionData formPositionData) {
    public static final int UNINITIALIZED_ID = -1;

    public TranslationAttribute {
        if (translationId == 0) {
            translationId = UNINITIALIZED_ID;
        }

        familiarityLevels = Collections.unmodifiableMap(familiarityLevels);
    }

    @Override
    public Map<Integer, Familiarity> familiarityLevels() {
        return Collections.unmodifiableMap(familiarityLevels);
    }
}
