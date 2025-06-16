package lule.dictionary.service.translation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.service.translation.dto.AddTranslationRequest;
import lule.dictionary.service.translation.dto.TranslationModel;
import lule.dictionary.service.translation.dto.FindTranslationRequest;
import lule.dictionary.service.translation.dto.UpdateFamiliarityRequest;
import lule.dictionary.dto.application.interfaces.imports.base.Import;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.factory.dto.TranslationFactory;
import lule.dictionary.factory.dto.UserProfileFactory;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.service.DocumentParsingService;
import lule.dictionary.service.StringParsingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationUtilService translationUtilService;
    private final StringParsingService stringParsingService;
    private final DocumentParsingService documentParsingService;

    public int add(RedirectAttributes redirectAttributes, @NonNull AddTranslationRequest addTranslationRequest) throws ServiceException {
        try {
            String transformedTargetWord = translationUtilService.transformInput(addTranslationRequest.targetWord());
            Translation translationToAdd = TranslationFactory.createTranslation(
                    TranslationFactory.createTranslationDetails(
                            addTranslationRequest.sourceWord(),
                            transformedTargetWord,
                            addTranslationRequest.familiarity()
                    ),
                    UserProfileFactory.createSettings(
                            addTranslationRequest.sourceLanguage(),
                            addTranslationRequest.targetLanguage()
                    ),
                    addTranslationRequest.owner()
            );
            int translationId = translationRepository.addTranslation(
                    translationToAdd.translationDetails(),
                    translationToAdd.userProfileSettings(),
                    translationToAdd.owner(),
                    addTranslationRequest.importId()).orElseThrow(() -> new ServiceException("Failed to add new translation"));
            redirectAttributes.addFlashAttribute("translationModel", new TranslationModel(
                    addTranslationRequest.importId(),
                    translationUtilService.getFamiliarityAsInt(addTranslationRequest.familiarity()),
                    translationToAdd,
                    translationUtilService.getSortedFamiliarityMap(),
                    addTranslationRequest.selectedWordId()
            ));
            return translationId;
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to add new translation", e.getCause());
        }
    }

    public List<Translation> findAllByOwner(@NonNull String owner) throws ServiceException{
        try {
            return translationRepository.findByOwner(owner);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch translations", e.getCause());
        }
    }

    public boolean findByTargetWord(@NonNull Model model, @NonNull FindTranslationRequest translationRequest) throws ServiceException{
        try {
            String cleanTargetWord = translationUtilService.transformInput(translationRequest.targetWord());
            if(cleanTargetWord.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "String is not in a valid state");

            if(translationRepository.findByTargetWord(cleanTargetWord).isPresent()) {
                Translation translation = translationRepository.findByTargetWord(cleanTargetWord).get();
                model.addAttribute("translationModel", new TranslationModel(
                        translationRequest.importId(),
                        translationUtilService.getFamiliarityAsInt(translation.translationDetails().familiarity()),
                        translation,
                        translationUtilService.getSortedFamiliarityMap(),
                        translationRequest.selectedWordId()
                ));
                return true;
            }

            Translation translation = TranslationFactory.createTranslation(
                    TranslationFactory.createTranslationDetails(
                            "translationFromApi",
                            translationUtilService.transformInput(cleanTargetWord),
                            Familiarity.UNKNOWN
                    ),
                    UserProfileFactory.createSettings(
                            Language.EN,
                            Language.NO
                    ),
                    "username"
            );
            model.addAttribute("translationModel", new TranslationModel(
                    translationRequest.importId(),
                    1,
                    translation,
                    translationUtilService.getSortedFamiliarityMap(),
                    translationRequest.selectedWordId()
            ));
            return false;
        }
        catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch translation for word " + translationRequest.targetWord(), e.getCause());
        }
    }

    public void updateSourceWord(@NonNull String sourceWord, @NonNull String targetWord) throws ServiceException{
        try {
            translationRepository.updateSourceWord(sourceWord, targetWord).orElseThrow(() -> new ServiceException("Failed to update source word for " + targetWord));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update source word for " + targetWord, e.getCause());
        }
    }

    public void updateFamiliarity(RedirectAttributes redirectAttributes, UpdateFamiliarityRequest updateFamiliarityRequest) throws ServiceException{
        try {
            String transformedTargetWord = translationUtilService.transformInput(updateFamiliarityRequest.targetWord());
            Translation translation = translationRepository.updateFamiliarity(transformedTargetWord, updateFamiliarityRequest.familiarity()).orElseThrow(() -> new ServiceException("Failed to update familiarity for " + transformedTargetWord));
            redirectAttributes.addFlashAttribute("translationModel", new TranslationModel(
                    updateFamiliarityRequest.importId(),
                    translationUtilService.getFamiliarityAsInt(translation.translationDetails().familiarity()),
                    translation,
                    translationUtilService.getSortedFamiliarityMap(),
                    updateFamiliarityRequest.selectedWordId()
            ));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update familiarity", e.getCause());
        }
    }

    public List<Translation> findByTargetWords(List<String> targetWords) {
        try {
            List<String> validTargetWords = targetWords.stream()
                    .map(word -> stringParsingService.removeNonLetters(word).trim().toLowerCase())
                    .filter(word -> !word.isEmpty())
                    .distinct()
                    .toList();
            return translationRepository.findByTargetWords(validTargetWords);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch translations", e.getCause());
        }
    }

    public Map<String, Translation> findTranslationsByImport(@NonNull Import imported) {
        try {
            List<String> targetWords = documentParsingService.parse(imported.importDetails().content());
            return findByTargetWords(targetWords).stream().collect(Collectors.toUnmodifiableMap((key) -> key.translationDetails().targetWord(), (value) -> value));
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }
}
