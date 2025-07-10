package lule.dictionary.service.userProfile;

import lombok.RequiredArgsConstructor;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.translation.TranslationServiceImp;
import org.springframework.stereotype.Service;

import static lule.dictionary.service.language.LanguageHelper.languageMapNames;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserDataService {

    private final TranslationServiceImp translationService;
    private final UserProfileService userProfileService;

    public String getFullName(Language language) {
        return languageMapNames.get(language);
    }

    public int getWordsLearned(String owner) {
        return translationService.getWordsLearnedCount(owner);
    }

    public int getDailyStreak(String owner) {
        return userProfileService.getDailyStreak(owner);
    }
}
