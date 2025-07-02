package lule.dictionary.service.translation;

import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.application.implementation.translation.base.DictionaryTranslation;
import lule.dictionary.entity.application.interfaces.translation.TranslationDetails;
import lule.dictionary.exception.RetryViewException;
import lule.dictionary.service.dto.ServiceResult;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.libreTranslate.LibreTranslateService;
import lule.dictionary.service.translation.dto.*;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.service.translation.exception.SourceWordNotFoundException;
import lule.dictionary.service.translation.exception.TranslationNotFoundException;
import lule.dictionary.service.util.StringRegexService;
import lule.dictionary.service.translation.util.TranslationFamiliarityService;
import lule.dictionary.util.errors.ErrorMapFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationFamiliarityService translationUtilService;
    private final StringRegexService stringRegexService;
    private final LibreTranslateService libreTranslateService;
    private final Validator validator;

    @Transactional
    public int add(Model model,
                   String owner,
                   @NonNull MutateTranslationRequest request) {
        if(!owner.equals(request.owner())) {
            throw new IllegalCallerException("Authentication name value does not match owner value");
        }
        var constraints = validator.validate(request);
        if(!constraints.isEmpty()) {
            model.addAttribute("translationModel", new TranslationModel(
                    request.importId(),
                    translationUtilService.getFamiliarityAsInt(request.familiarity()),
                    null,
                    translationUtilService.getSortedFamiliarityMap(),
                    request.selectedWordId(),
                    request.page()
            ));
            model.addAttribute("result", new ServiceResult(true, ErrorMapFactory.fromSet(constraints)));
            throw new IllegalArgumentException("Constraints violated at " + request);
        }
        String transformedTargetWord = stringRegexService.removeNonLetters(request.targetWord());
        Translation translationToAdd = DictionaryTranslation.builder()
                .sourceWords(request.sourceWords())
                .targetWord(transformedTargetWord)
                .familiarity(request.familiarity())
                .sourceLanguage(request.sourceLanguage())
                .targetLanguage(request.targetLanguage())
                .owner(request.owner())
                .build();
        int translationId = translationRepository.addTranslation(
                translationToAdd,
                request.importId()).orElseThrow(() -> new IllegalArgumentException("Failed to add new translation"));

        model.addAttribute("translationModel", new TranslationModel(
                request.importId(),
                translationUtilService.getFamiliarityAsInt(request.familiarity()),
                translationToAdd,
                translationUtilService.getSortedFamiliarityMap(),
                request.selectedWordId(),
                request.page()
        ));
        model.addAttribute("result", new ServiceResult(false, Map.of()));
        return translationId;
    }

    public List<Translation> findAllByOwner(@NonNull String owner){
        return translationRepository.findByOwner(owner);
    }

    @Transactional
    public void findByTargetWord(@NonNull Model model, @NonNull
                                 FindTranslationRequest request,
                                 String owner,
                                 Language sourceLanguage,
                                 Language targetLanguage) {
        var constraints = validator.validate(request);
        if(!constraints.isEmpty()) {
            model.addAttribute("result", new ServiceResult(true, ErrorMapFactory.fromSet(constraints)));
            throw new RetryViewException("Constraints violated at " + request);
        }
        String cleanTargetWord = stringRegexService.removeNonLetters(request.targetWord());
        if(cleanTargetWord.isEmpty()) throw new RetryViewException("Target word contains illegal characters");
        if(translationRepository.findByTargetWord(cleanTargetWord, owner).isPresent()) {
            Translation translation = translationRepository.findByTargetWord(cleanTargetWord, owner).get();
            model.addAttribute("translationModel", new TranslationModel(
                    request.importId(),
                    translationUtilService.getFamiliarityAsInt(translation.familiarity()),
                    translation,
                    translationUtilService.getSortedFamiliarityMap(),
                    request.selectedWordId(),
                    request.page()
            ));
            return;
        }
        List<String> libreTranslateSourceWords = libreTranslateService.translate(
                stringRegexService.removeNonLetters(cleanTargetWord),
                sourceLanguage,
                targetLanguage
        );
        List<String> dbSourceWords = translationRepository.findMostFrequentSourceWords(cleanTargetWord, 3);
        Translation translation = DictionaryTranslation.builder()
                .sourceWords(Stream.concat(
                        libreTranslateSourceWords.stream(),
                        dbSourceWords.stream()
                )
                .distinct()
                .toList())
                .targetWord(stringRegexService.removeNonLetters(cleanTargetWord))
                .familiarity(Familiarity.UNKNOWN)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .owner(owner)
                .build();
        model.addAttribute("translationModel", new TranslationModel(
                request.importId(),
                1,
                translation,
                translationUtilService.getSortedFamiliarityMap(),
                request.selectedWordId(),
                request.page()
        ));
        throw new TranslationNotFoundException("Translation not found");
    }

    @Transactional
    public void updateFamiliarity(Model model, UpdateTranslationFamiliarityRequest request){
        String transformedTargetWord = stringRegexService.removeNonLetters(request.targetWord());
        Translation translation = translationRepository.updateFamiliarity(
                transformedTargetWord,
                request.familiarity(),
                request.owner()
                ).orElseThrow(() -> new TranslationNotFoundException("Failed to update familiarity for " + transformedTargetWord));
        model.addAttribute("translationModel", new TranslationModel(
                request.importId(),
                translationUtilService.getFamiliarityAsInt(translation.familiarity()),
                translation,
                translationUtilService.getSortedFamiliarityMap(),
                request.selectedWordId(),
                request.page()
        ));
    }

    public List<Translation> findByTargetWords(List<String> targetWords,
                                               String owner) {
        List<String> validTargetWords = targetWords.stream()
                .map(word -> stringRegexService.removeNonLetters(word).trim().toLowerCase())
                .filter(word -> !word.isEmpty())
                .distinct()
                .toList();
        return translationRepository.findByTargetWords(validTargetWords, owner);
    }

    public Map<String, Translation> findTranslationsByImport(@NonNull Import imported,
                                                             String owner) {
        List<String> targetWords = Arrays.stream(imported.content().split(" "))
                .map(stringRegexService::removeNonLetters)
                .toList();
        return findByTargetWords(targetWords, owner).stream()
                .collect(Collectors.toUnmodifiableMap(
                    TranslationDetails::targetWord,
                    (value) -> value,
                    (first, second) -> first
                ));
    }

    @Transactional
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
            throw new RetryViewException("Constraints violated at " + request);
        }
        Optional<Translation> translation = translationRepository.updateSourceWords(
                request.sourceWords(),
                request.targetWord(),
                request.owner()
        );
        translation.ifPresent(value -> {
            model.addAttribute("targetWord", value.targetWord());
            model.addAttribute("sourceWords", value.sourceWords());
            model.addAttribute("result", new ServiceResult(false, Map.of()));
        });
        model.addAttribute("targetWord", request.targetWord());
        model.addAttribute("sourceWords", request.sourceWords());
        model.addAttribute("result", new ServiceResult(false, Map.of()));
    }

    @Transactional
    public void deleteSourceWord(Model model, DeleteSourceWordRequest request) {
        var constraints = validator.validate(request);
        if(!constraints.isEmpty()) {
            model.addAttribute("result", new ServiceResult(true, ErrorMapFactory.fromSet(constraints)));
            throw new RetryViewException("Constraints violated at " + request);
        }
        Optional<Translation> translation = translationRepository.deleteSourceWord(
                request.sourceWord(),
                request.targetWord(),
                request.owner()
        );
        if(translation.isPresent()) {
            model.addAttribute("targetWord", request.targetWord());
            model.addAttribute("sourceWords", translation.get().sourceWords());
            model.addAttribute("result", new ServiceResult(false, Map.of()));
            return;
        }
        model.addAttribute("sourceWords", List.of());
        throw new SourceWordNotFoundException("Source word not found");
    }

    public int getWordsLearnedCount(String owner) {
        return translationRepository.getWordsLearnedCount(owner);
    }
}
