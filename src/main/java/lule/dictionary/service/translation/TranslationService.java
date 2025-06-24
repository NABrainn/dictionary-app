package lule.dictionary.service.translation;

import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.translation.base.DictionaryTranslation;
import lule.dictionary.entity.application.interfaces.translation.TranslationDetails;
import lule.dictionary.service.libreTranslate.LibreTranslateService;
import lule.dictionary.service.translation.dto.*;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.service.util.StringRegexService;
import lule.dictionary.service.translation.util.TranslationFamiliarityService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationFamiliarityService translationUtilService;
    private final StringRegexService stringRegexService;
    private final LibreTranslateService libreTranslateService;
    private final Validator validator;

    public int add(Model model,
                   @NonNull MutateTranslationRequest mutateTranslationRequest) {
        try {
            String transformedTargetWord = stringRegexService.removeNonLetters(mutateTranslationRequest.targetWord());
            Translation translationToAdd = DictionaryTranslation.builder()
                    .sourceWords(mutateTranslationRequest.sourceWords())
                    .targetWord(transformedTargetWord)
                    .familiarity(mutateTranslationRequest.familiarity())
                    .sourceLanguage(mutateTranslationRequest.sourceLanguage())
                    .targetLanguage(mutateTranslationRequest.targetLanguage())
                    .owner(mutateTranslationRequest.owner())
                    .build();
            int translationId = translationRepository.addTranslation(
                    translationToAdd,
                    mutateTranslationRequest.importId()).orElseThrow(() -> new ServiceException("Failed to add new translation"));
            model.addAttribute("translationModel", new TranslationModel(
                    mutateTranslationRequest.importId(),
                    translationUtilService.getFamiliarityAsInt(mutateTranslationRequest.familiarity()),
                    translationToAdd,
                    translationUtilService.getSortedFamiliarityMap(),
                    mutateTranslationRequest.selectedWordId()
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

    public boolean findByTargetWord(@NonNull Authentication authentication,
                                    @NonNull Model model, @NonNull
                                    FindTranslationRequest translationRequest) throws ServiceException{
        try {
            String cleanTargetWord = stringRegexService.removeNonLetters(translationRequest.targetWord());
            if(cleanTargetWord.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "String is not in a valid state");

            if(translationRepository.findByTargetWord(cleanTargetWord).isPresent()) {
                Translation translation = translationRepository.findByTargetWord(cleanTargetWord).get();
                model.addAttribute("translationModel", new TranslationModel(
                        translationRequest.importId(),
                        translationUtilService.getFamiliarityAsInt(translation.familiarity()),
                        translation,
                        translationUtilService.getSortedFamiliarityMap(),
                        translationRequest.selectedWordId()
                ));
                return true;
            }
            List<String> sourceWords = libreTranslateService.translate(
                    stringRegexService.removeNonLetters(cleanTargetWord),
                    Language.EN,
                    Language.NO
            );
            Translation translation = DictionaryTranslation.builder()
                    .sourceWords(sourceWords)
                    .targetWord(stringRegexService.removeNonLetters(cleanTargetWord))
                    .familiarity(Familiarity.UNKNOWN)
                    .sourceLanguage(Language.EN)
                    .targetLanguage(Language.NO)
                    .owner(authentication.getName())
                    .build();
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

    public void updateFamiliarity(Model model, UpdateTranslationFamiliarityRequest updateFamiliarityDto) throws ServiceException{
        try {
            String transformedTargetWord = stringRegexService.removeNonLetters(updateFamiliarityDto.targetWord());
            Translation translation = translationRepository.updateFamiliarity(
                    transformedTargetWord,
                    updateFamiliarityDto.familiarity()).orElseThrow(() -> new ServiceException("Failed to update familiarity for " + transformedTargetWord));
            model.addAttribute("translationModel", new TranslationModel(
                    updateFamiliarityDto.importId(),
                    translationUtilService.getFamiliarityAsInt(translation.familiarity()),
                    translation,
                    translationUtilService.getSortedFamiliarityMap(),
                    updateFamiliarityDto.selectedWordId()
            ));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update familiarity", e.getCause());
        }
    }

    public List<Translation> findByTargetWords(List<String> targetWords) {
        try {
            List<String> validTargetWords = targetWords.stream()
                    .map(word -> stringRegexService.removeNonLetters(word).trim().toLowerCase())
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
            List<String> targetWords = Arrays.stream(imported.content().split(" "))
                    .map(stringRegexService::removeNonLetters)
                    .toList();
            return findByTargetWords(targetWords).stream().collect(Collectors.toUnmodifiableMap(TranslationDetails::targetWord, (value) -> value));
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    public void updateSourceWords(Model model, UpdateSourceWordsRequest request) {
        Optional<Translation> translation = translationRepository.updateSourceWords(request.sourceWords(), request.targetWord());
        translation.ifPresent(value -> model.addAttribute("sourceWords", value.sourceWords()));

    }

    public void deleteSourceWord(Model model, DeleteSourceWordRequest deleteSourceWordRequest) {
        Optional<Translation> translation = translationRepository.deleteSourceWord(deleteSourceWordRequest.sourceWord(), deleteSourceWordRequest.targetWord());
        if(translation.isPresent()) {
            model.addAttribute("sourceWords", translation.get().sourceWords());
        }
        else {
            model.addAttribute("sourceWords", List.of());
        }
    }
}
