package lule.dictionary.service.userProfile;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.translation.TranslationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserDataService {

    private final TranslationService translationService;

    private final Map<Language, String> languageFullNameMap = Map.of(
            Language.NO, "Norwegian",
            Language.PL, "Polish",
            Language.EN, "English"
    );

    public String getFullName(Language language) {
        return languageFullNameMap.get(language);
    }

    public int getWordsLearned(String owner) {
        return translationService.getWordsLearnedCount(owner);
    }
}
