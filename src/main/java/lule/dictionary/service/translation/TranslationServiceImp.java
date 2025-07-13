package lule.dictionary.service.translation;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.database.implementation.translation.base.TranslationImp;
import lule.dictionary.dto.database.interfaces.imports.base.Import;
import lule.dictionary.dto.database.interfaces.translation.TranslationDetails;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.dto.application.result.ServiceResultImp;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.libreTranslate.LibreTranslateService;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.translation.dto.attribute.TranslationPair;
import lule.dictionary.service.translation.dto.request.*;
import lule.dictionary.service.translation.exception.TranslationNotFoundException;
import lule.dictionary.service.validation.ValidationService;
import lule.dictionary.util.errors.ErrorMapFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationServiceImp implements TranslationService {

    private final TranslationRepository translationRepository;
    private final LibreTranslateService libreTranslateService;
    private final ValidationService validationService;

    @Transactional
    public ServiceResult<TranslationAttribute> createTranslation(@NonNull AddTranslationRequest request) throws InvalidInputException {
        try {
            validate(request);
            Translation translation = TranslationImp.builder()
                    .sourceWords(request.sourceWords())
                    .targetWord(request.targetWord())
                    .familiarity(request.familiarity())
                    .sourceLanguage(request.sourceLanguage())
                    .targetLanguage(request.targetLanguage())
                    .owner(request.owner())
                    .build();
            int translationId = insertIntoDatabase(translation, request.importId());
            return ServiceResultImp.success(TranslationAttribute.builder()
                    .importId(request.importId())
                    .selectedWordId(request.selectedWordId())
                    .translationId(translationId)
                    .translation(translation)
                    .currentFamiliarity(getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .page(request.page())
                    .build());
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(ServiceResultImp.error(TranslationAttribute.builder()
                    .importId(request.importId())
                    .selectedWordId(request.selectedWordId())
                    .translation(null)
                    .currentFamiliarity(getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .page(request.page())
                    .translationId(-1)
                    .build(), ErrorMapFactory.fromViolations(e.getConstraintViolations())));
        }
    }

    @Transactional
    public ServiceResult<TranslationAttribute> findByTargetWord(@NonNull FindByTargetWordRequest request) throws TranslationNotFoundException, InvalidInputException {
        try {
            validate(request);
            if(translationRepository.findByTargetWord(request.targetWord(), request.owner()).isPresent()) {
                Translation translation = translationRepository.findByTargetWord(request.targetWord(), request.owner()).get();
                return ServiceResultImp.success(TranslationAttribute.builder()
                        .importId(request.importId())
                        .selectedWordId(request.selectedWordId())
                        .translation(translation)
                        .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                        .familiarityLevels(getFamiliarityTable())
                        .page(request.page())
                        .translationId(-1)
                        .build());
            }
            List<String> sourceWordsFromLibreTranslate = libreTranslateService.translate(
                    request.targetWord(),
                    request.sourceLanguage(),
                    request.targetLanguage()
            );
            List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(request.targetWord(), 3);
            Translation translation = TranslationImp.builder()
                    .sourceWords(mergeSourceWordLists(sourceWordsFromDatabase, sourceWordsFromLibreTranslate))
                    .targetWord(request.targetWord())
                    .familiarity(Familiarity.UNKNOWN)
                    .sourceLanguage(request.sourceLanguage())
                    .targetLanguage(request.targetLanguage())
                    .owner(request.owner())
                    .build();
            throw new TranslationNotFoundException(ServiceResultImp.error(TranslationAttribute.builder()
                    .importId(request.importId())
                    .selectedWordId(request.selectedWordId())
                    .translation(translation)
                    .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .page(request.page())
                    .translationId(-1)
                    .build(), Map.of()));
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(ServiceResultImp.errorEmpty(ErrorMapFactory.fromViolations(e.getConstraintViolations())));
        }
    }

    @Transactional
    public ServiceResult<TranslationAttribute> updateFamiliarity(UpdateTranslationFamiliarityRequest request) {
        String targetWordWithOnlyLetters = request.targetWord();
        Translation translation = translationRepository.updateFamiliarity(targetWordWithOnlyLetters, request.familiarity(), request.owner())
                .orElseThrow(() -> new RuntimeException("Failed to update familiarity for " + targetWordWithOnlyLetters));
        return ServiceResultImp.success(TranslationAttribute.builder()
                .importId(request.importId())
                .selectedWordId(request.selectedWordId())
                .translation(translation)
                .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                .familiarityLevels(getFamiliarityTable())
                .page(request.page())
                .translationId(-1)
                .build());
    }

    @Transactional
    public ServiceResult<TranslationPair> updateSourceWords(UpdateSourceWordsRequest request) throws InvalidInputException {
        try {
            validate(request);
            Optional<Translation> translation = translationRepository.updateSourceWords(
                    request.sourceWords(),
                    request.targetWord(),
                    request.owner()
            );
            if(translation.isPresent())
                return ServiceResultImp.success(TranslationPair.of(translation.get().sourceWords(), translation.get().targetWord()));
            throw new RuntimeException("Unknown exception");
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(ServiceResultImp.error(TranslationPair.of(request.sourceWords()
                    .stream()
                    .filter(word -> !word.isBlank())
                    .filter(word -> compileNonSpecialChars().matcher(word).matches())
                    .toList(), request.targetWord()), ErrorMapFactory.fromViolations(e.getConstraintViolations())));
        }
    }

    @Transactional
    public ServiceResult<TranslationPair> deleteSourceWord(DeleteSourceWordRequest request) {
        try {
            validate(request);
            Optional<Translation> translation = translationRepository.deleteSourceWord(
                    request.sourceWord(),
                    request.targetWord(),
                    request.owner()
            );
            if(translation.isPresent())
                return ServiceResultImp.success(TranslationPair.of(translation.get().sourceWords(), translation.get().targetWord()));
            throw new RuntimeException("Unknown exception");
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(ServiceResultImp.errorEmpty(ErrorMapFactory.fromViolations(e.getConstraintViolations())));
        }
    }

    @Override
    public ServiceResult<Integer> getWordsLearnedCount(String owner, Language targetLanguage) {
        return ServiceResultImp.success(translationRepository.getWordsLearnedCount(owner, targetLanguage));
    }


    @Override
    public ServiceResult<Map<String, Translation>> findTranslationsByImport(@NonNull Import imported, String owner) {
        List<String> targetWords = Arrays.stream(imported.content().split(" ")).toList();
        return ServiceResultImp.success(findByTargetWords(targetWords, owner).stream()
                .collect(Collectors.toUnmodifiableMap(
                                TranslationDetails::targetWord,
                                (value) -> value,
                                (first, second) -> first
                        )
                ));
    }


    private List<Translation> findByTargetWords(List<String> targetWords,
                                                String owner) {
        List<String> validTargetWords = targetWords.stream()
                .map(word -> word.trim().toLowerCase())
                .filter(word -> !word.isEmpty())
                .distinct()
                .toList();
        return translationRepository.findByTargetWords(validTargetWords, owner);
    }

    private Pattern compileNonSpecialChars() {
        return Pattern.compile("^[\\p{L}0-9 ]+$");
    }

    private List<String> mergeSourceWordLists(List<String> sourceWordsFromDatabase, List<String> sourceWordsFromLibreTranslate) {
        return Stream.concat(
                        sourceWordsFromDatabase.stream(),
                        sourceWordsFromLibreTranslate.stream()
                )
                .distinct()
                .toList();
    }

    private int insertIntoDatabase(Translation translation,  int importId) {
        return translationRepository.addTranslation(translation, importId).orElseThrow(() -> new RuntimeException("Failed to add new translation"));
    }

    private void validate(ServiceRequest request) throws ConstraintViolationException {
        validationService.validate(request);
    }

    private int getFamiliarityAsDigit(Familiarity familiarity) {
        return switch (familiarity) {
            case UNKNOWN -> 1;
            case RECOGNIZED -> 2;
            case FAMILIAR -> 3;
            case KNOWN -> 4;
            case IGNORED -> 5;
        };
    }

    private Map<Integer, Familiarity> getFamiliarityTable() {
        return new TreeMap<>(Map.of(
                1, Familiarity.UNKNOWN,
                2, Familiarity.RECOGNIZED,
                3, Familiarity.FAMILIAR,
                4, Familiarity.KNOWN,
                5, Familiarity.IGNORED)
        );
    }
}
