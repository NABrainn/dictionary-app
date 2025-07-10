package lule.dictionary.service.translation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.timezone.TimeZoneOffsetContext;
import lule.dictionary.entity.application.implementation.translation.base.TranslationImp;
import lule.dictionary.entity.application.interfaces.translation.TranslationDetails;
import lule.dictionary.exception.RetryViewException;
import lule.dictionary.service.dto.result.ServiceResultImp;
import lule.dictionary.service.dto.result.ServiceResult;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.libreTranslate.LibreTranslateService;
import lule.dictionary.service.translation.dto.*;
import lule.dictionary.entity.application.interfaces.imports.base.Import;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.service.translation.dto.request.AddTranslationRequest;
import lule.dictionary.service.translation.exception.SourceWordNotFoundException;
import lule.dictionary.service.translation.exception.TranslationNotFoundException;
import lule.dictionary.service.userProfile.UserProfileService;
import lule.dictionary.service.util.StringRegexService;
import lule.dictionary.service.translation.util.TranslationFamiliarityService;
import lule.dictionary.service.validation.ValidationService;
import lule.dictionary.util.errors.ErrorMapFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationServiceImp implements TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationFamiliarityService translationUtilService;
    private final StringRegexService stringRegexService;
    private final LibreTranslateService libreTranslateService;
    private final UserProfileService userProfileService;
    private final Validator validator;
    private final ValidationService validationService;

    @Transactional
    public ServiceResult<TranslationAttribute> createTranslation(@NonNull AddTranslationRequest request) {
        try {
            validationService.validate(request);
            Translation translation = TranslationImp.builder()
                    .sourceWords(request.sourceWords())
                    .targetWord(request.targetWord())
                    .familiarity(request.familiarity())
                    .sourceLanguage(request.sourceLanguage())
                    .targetLanguage(request.targetLanguage())
                    .owner(request.owner())
                    .build();
            int translationId = translationRepository.addTranslation(translation, request.importId()).orElseThrow(() -> new RuntimeException("Failed to add new translation"));
            return ServiceResultImp.success(TranslationAttribute.builder()
                    .importId(request.importId())
                    .selectedWordId(request.selectedWordId())
                    .translationId(translationId)
                    .translation(translation)
                    .currentFamiliarity(getFamiliarityAsInt(request.familiarity()))
                    .familiarityLevels(getSortedFamiliarityMap())
                    .page(request.page())
                    .build());
        } catch (ConstraintViolationException e) {
            return ServiceResultImp.error(TranslationAttribute.builder()
                    .importId(request.importId())
                    .selectedWordId(request.selectedWordId())
                    .translationId(-1)
                    .translation(null)
                    .currentFamiliarity(getFamiliarityAsInt(request.familiarity()))
                    .familiarityLevels(getSortedFamiliarityMap())
                    .page(request.page())
                    .build(), ErrorMapFactory.fromViolations(e.getConstraintViolations()));
        }
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
            model.addAttribute("result", new ServiceResultImp(true, ErrorMapFactory.fromViolations(constraints)));
            throw new RetryViewException("Constraints violated at " + request);
        }
        String cleanTargetWord = stringRegexService.removeNonLetters(request.targetWord());
        if(cleanTargetWord.isEmpty()) throw new RetryViewException("Target word contains illegal characters");
        if(translationRepository.findByTargetWord(cleanTargetWord, owner).isPresent()) {
            Translation translation = translationRepository.findByTargetWord(cleanTargetWord, owner).get();
            model.addAttribute("translationAttribute", new TranslationAttribute(
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
        Translation translation = TranslationImp.builder()
                .sourceWords(Stream.concat(
                        dbSourceWords.stream(),
                        libreTranslateSourceWords.stream()
                )
                .distinct()
                .toList())
                .targetWord(stringRegexService.removeNonLetters(cleanTargetWord))
                .familiarity(Familiarity.UNKNOWN)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .owner(owner)
                .build();
        model.addAttribute("translationAttribute", new TranslationAttribute(
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
        model.addAttribute("translationAttribute", new TranslationAttribute(
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
    public void updateSourceWords(Model model,
                                  UpdateSourceWordsRequest request) {
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
            model.addAttribute("result", new ServiceResultImp(true, ErrorMapFactory.fromSetConcrete(constraints)));
            throw new RetryViewException("Constraints violated at " + request);
        }
        Optional<Translation> translation = translationRepository.updateSourceWords(
                request.sourceWords(),
                request.targetWord(),
                request.owner()
        );
        if(TimeZoneOffsetContext.get() != null) {
            userProfileService.updateTimezoneOffset(
                    request.owner(),
                    TimeZoneOffsetContext.get()
            );
        }
        translation.ifPresent(value -> {
            model.addAttribute("targetWord", value.targetWord());
            model.addAttribute("sourceWords", value.sourceWords());
            model.addAttribute("result", new ServiceResultImp(false, Map.of()));
        });
        model.addAttribute("targetWord", request.targetWord());
        model.addAttribute("sourceWords", request.sourceWords());
        model.addAttribute("result", new ServiceResultImp(false, Map.of()));
    }

    @Transactional
    public void deleteSourceWord(Model model, DeleteSourceWordRequest request) {
        var constraints = validator.validate(request);
        if(!constraints.isEmpty()) {
            model.addAttribute("result", new ServiceResultImp(true, ErrorMapFactory.fromSetConcrete(constraints)));
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
            model.addAttribute("result", new ServiceResultImp(false, Map.of()));
            return;
        }
        model.addAttribute("sourceWords", List.of());
        throw new SourceWordNotFoundException("Source word not found");
    }

    public int getWordsLearnedCount(String owner) {
        return translationRepository.getWordsLearnedCount(owner);
    }

    private int getFamiliarityAsInt(Familiarity familiarity) {
        return switch (familiarity) {
            case UNKNOWN -> 1;
            case RECOGNIZED -> 2;
            case FAMILIAR -> 3;
            case KNOWN -> 4;
            case IGNORED -> 5;
        };
    }

    private Map<Integer, Familiarity> getSortedFamiliarityMap() {
        return new TreeMap<>(Map.of(
                1, Familiarity.UNKNOWN,
                2, Familiarity.RECOGNIZED,
                3, Familiarity.FAMILIAR,
                4, Familiarity.KNOWN,
                5, Familiarity.IGNORED)
        );
    }
}
