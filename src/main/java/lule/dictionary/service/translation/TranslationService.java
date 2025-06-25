package lule.dictionary.service.translation;

import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.translation.base.DictionaryTranslation;
import lule.dictionary.entity.application.interfaces.translation.TranslationDetails;
import lule.dictionary.exception.ResourceNotFoundException;
import lule.dictionary.service.dto.ServiceResult;
import lule.dictionary.service.libreTranslate.LibreTranslateService;
import lule.dictionary.service.translation.dto.*;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;
import lule.dictionary.exception.ServiceException;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.service.util.StringRegexService;
import lule.dictionary.service.translation.util.TranslationFamiliarityService;
import lule.dictionary.util.errors.ErrorMapFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.regex.Pattern;
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
                   Authentication authentication,
                   @NonNull MutateTranslationRequest mutateTranslationRequest) {
        if(!authentication.getName().equals(mutateTranslationRequest.owner())) {
            throw new ServiceException("Authentication name value does not match owner value");
        }
        var constraints = validator.validate(mutateTranslationRequest);
        if(!constraints.isEmpty()) {
            model.addAttribute("translationModel", new TranslationModel(
                    mutateTranslationRequest.importId(),
                    translationUtilService.getFamiliarityAsInt(mutateTranslationRequest.familiarity()),
                    null,
                    translationUtilService.getSortedFamiliarityMap(),
                    mutateTranslationRequest.selectedWordId()
            ));
            model.addAttribute("result", new ServiceResult(true, ErrorMapFactory.fromSet(constraints)));
            throw new ServiceException("Constraints violated at " + mutateTranslationRequest);
        }
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
        model.addAttribute("result", new ServiceResult(false, Map.of()));
        return translationId;
    }

    public List<Translation> findAllByOwner(@NonNull String owner) throws ServiceException{
        return translationRepository.findByOwner(owner);
    }

    public boolean findByTargetWord(@NonNull Authentication authentication,
                                    @NonNull Model model, @NonNull
                                    FindTranslationRequest translationRequest) throws ServiceException{
        var constraints = validator.validate(translationRequest);
        if(!constraints.isEmpty()) {
            model.addAttribute("result", new ServiceResult(true, ErrorMapFactory.fromSet(constraints)));
            throw new ServiceException("Constraints violated at " + translationRequest);
        }
        String cleanTargetWord = stringRegexService.removeNonLetters(translationRequest.targetWord());
        if(cleanTargetWord.isEmpty()) throw new ServiceException("String is not in a valid state");
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

    public void updateFamiliarity(Model model, UpdateTranslationFamiliarityRequest updateFamiliarityDto) throws ServiceException{
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
    }

    public List<Translation> findByTargetWords(List<String> targetWords) {
        List<String> validTargetWords = targetWords.stream()
                .map(word -> stringRegexService.removeNonLetters(word).trim().toLowerCase())
                .filter(word -> !word.isEmpty())
                .distinct()
                .toList();
        return translationRepository.findByTargetWords(validTargetWords);
    }

    public Map<String, Translation> findTranslationsByImport(@NonNull Import imported) {
        List<String> targetWords = Arrays.stream(imported.content().split(" "))
                .map(stringRegexService::removeNonLetters)
                .toList();
        return findByTargetWords(targetWords).stream().collect(Collectors.toUnmodifiableMap(TranslationDetails::targetWord, (value) -> value));
    }

    public void updateSourceWords(Model model, UpdateSourceWordsRequest request) {
        var constraints = validator.validate(request);
        Pattern validWordPattern = Pattern.compile("^[\\p{L}0-9 ]+$");

        if (!constraints.isEmpty()) {
            model.addAttribute("targetWord", request.targetWord());
            model.addAttribute("sourceWords", request.sourceWords()
                    .stream()
                    .filter(word -> !word.isBlank())
                    .filter(word -> validWordPattern.matcher(word).matches())
                    .toList()
            );
            model.addAttribute("result", new ServiceResult(true, ErrorMapFactory.fromSet(constraints)));
            throw new ServiceException("Constraints violated at " + request);
        }
        Optional<Translation> translation = translationRepository.updateSourceWords(request.sourceWords(), request.targetWord());
        translation.ifPresent(value -> {
            model.addAttribute("targetWord", value.targetWord());
            model.addAttribute("sourceWords", value.sourceWords());
            model.addAttribute("result", new ServiceResult(false, Map.of()));
        });
        model.addAttribute("targetWord", request.targetWord());
        model.addAttribute("sourceWords", request.sourceWords());
        model.addAttribute("result", new ServiceResult(false, Map.of()));
    }

    public void deleteSourceWord(Model model, DeleteSourceWordRequest deleteSourceWordRequest) {
        var constraints = validator.validate(deleteSourceWordRequest);
        if(!constraints.isEmpty()) {
            model.addAttribute("result", new ServiceResult(true, ErrorMapFactory.fromSet(constraints)));
            throw new ServiceException("Constraints violated at " + deleteSourceWordRequest);
        }
        Optional<Translation> translation = translationRepository.deleteSourceWord(deleteSourceWordRequest.sourceWord(), deleteSourceWordRequest.targetWord());
        if(translation.isPresent()) {
            model.addAttribute("targetWord", deleteSourceWordRequest.targetWord());
            model.addAttribute("sourceWords", translation.get().sourceWords());
            model.addAttribute("result", new ServiceResult(false, Map.of()));
            return;
        }
        model.addAttribute("sourceWords", List.of());
        throw new ResourceNotFoundException("Source word not found");
    }
}
