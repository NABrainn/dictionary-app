package lule.dictionary.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.repository.ImportRepository;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final UserProfileService userProfileService;
    private final ImportService importService;
    private final TranslationService translationService;


}
