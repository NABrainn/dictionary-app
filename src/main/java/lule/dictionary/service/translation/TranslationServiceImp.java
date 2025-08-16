package lule.dictionary.service.translation;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.controller.vocabularyController.dto.BaseFlashcardAttribute;
import lule.dictionary.controller.vocabularyController.dto.GetRandomTranslationsRequest;
import lule.dictionary.dto.database.implementation.translation.base.TranslationImp;
import lule.dictionary.dto.database.interfaces.translation.TranslationDetails;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;
import lule.dictionary.exception.application.InvalidInputException;
import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.dto.application.result.ServiceResultImp;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.dto.database.interfaces.translation.Translation;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.repository.TranslationRepository;
import lule.dictionary.service.translation.dto.attribute.TranslationAttribute;
import lule.dictionary.service.translation.dto.request.*;
import lule.dictionary.service.translation.exception.TranslationNotFoundException;
import lule.dictionary.service.translationFetching.TranslationFetchingService;
import lule.dictionary.service.validation.ValidationService;
import lule.dictionary.util.errors.ErrorFactory;
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
    private final TranslationFetchingService translationFetchingService;
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
                    .isPhrase(request.isPhrase())
                    .build();
            int translationId = insertIntoDatabase(translation, request);
            TranslationAttribute translationAttribute = TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translationId(translationId)
                    .translation(translation)
                    .currentFamiliarity(getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .build();
            return ServiceResultImp.success(translationAttribute);
        } catch (ConstraintViolationException e) {
            TranslationAttribute translationAttribute = TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translation(null)
                    .currentFamiliarity(getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .translationId(-1)
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .build();
            throw new InvalidInputException(ServiceResultImp.error(translationAttribute, ErrorFactory.fromViolations(e.getConstraintViolations())));
        }
    }

    @Transactional
    public ServiceResult<TranslationAttribute> findByTargetWord(@NonNull FindByTargetWordRequest request) throws TranslationNotFoundException, InvalidInputException {
        try {
            validate(request);
            Optional<Translation> optionalTranslation = getTranslationFromDatabase(request);
            if(optionalTranslation.isPresent()) {
                Translation translation = optionalTranslation.get();
                TranslationAttribute translationAttribute = TranslationAttribute.builder()
                        .selectedWordId(request.selectedWordId())
                        .translation(translation)
                        .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                        .familiarityLevels(getFamiliarityTable())
                        .translationId(-1)
                        .documentId(request.documentId())
                        .isPhrase(request.isPhrase())
                        .build();
                return ServiceResultImp.success(translationAttribute);
            }
            List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(request.targetWord(), 3);

            Translation translation = TranslationImp.builder()
                    .sourceWords(mergeSourceWordLists(
                            sourceWordsFromDatabase,
                            translationFetchingService.fetchTranslationsAsync(request.sourceLanguage(), request.targetLanguage(), request.targetWord())
                    ).stream().limit(3).toList())
                    .targetWord(request.targetWord())
                    .familiarity(Familiarity.UNKNOWN)
                    .sourceLanguage(request.sourceLanguage())
                    .targetLanguage(request.targetLanguage())
                    .owner(request.owner())
                    .isPhrase(request.isPhrase())
                    .build();
            TranslationAttribute translationAttribute = TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translation(translation)
                    .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .translationId(-1)
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .build();
            return ServiceResultImp.success(translationAttribute);
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(ServiceResultImp.error(ErrorFactory.fromViolations(e.getConstraintViolations())));
        }
    }

    @Transactional
    public ServiceResult<TranslationAttribute> updateFamiliarity(UpdateTranslationFamiliarityRequest request) {
        Translation translation = translationRepository.updateFamiliarity(request)
                .orElseThrow(() -> new RuntimeException("Failed to update familiarity for " + request.targetWord()));
        TranslationAttribute translationAttribute = TranslationAttribute.builder()
                .selectedWordId(request.selectedWordId())
                .translation(translation)
                .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                .familiarityLevels(getFamiliarityTable())
                .translationId(-1)
                .documentId(-1)
                .isPhrase(request.isPhrase())
                .build();
        return ServiceResultImp.success(translationAttribute);
    }

    @Transactional
    public ServiceResult<TranslationAttribute> updateSourceWords(UpdateSourceWordsRequest request) throws InvalidInputException {
        try {
            validate(request);
            Optional<Translation> optionalTranslation = executeDatabaseUpdate(request);
            if(optionalTranslation.isPresent()) {
                TranslationAttribute translationAttribute = TranslationAttribute.builder()
                        .documentId(-1)
                        .selectedWordId(request.selectedWordId())
                        .translationId(-1)
                        .translation(optionalTranslation.get())
                        .currentFamiliarity(getFamiliarityAsDigit(optionalTranslation.get().familiarity()))
                        .familiarityLevels(getFamiliarityTable())
                        .isPhrase(request.isPhrase())
                        .build();
                return ServiceResultImp.success(translationAttribute);
            }
            throw new RuntimeException("Unknown exception");
        } catch (ConstraintViolationException e) {
            Translation translation = TranslationImp.builder()
                    .sourceWords(request.sourceWords()
                            .stream()
                            .filter(word -> !word.isBlank())
                            .filter(word -> compileNonSpecialChars().matcher(word).matches())
                            .toList()
                    )
                    .targetWord(request.targetWord())
                    .familiarity(request.familiarity())
                    .sourceLanguage(request.sourceLanguage())
                    .targetLanguage(request.targetLanguage())
                    .owner(request.owner())
                    .isPhrase(request.isPhrase())
                    .build();
            TranslationAttribute translationAttribute = TranslationAttribute.builder()
                    .documentId(-1)
                    .selectedWordId(request.selectedWordId())
                    .translationId(-1)
                    .translation(translation)
                    .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .isPhrase(request.isPhrase())
                    .build();
            throw new InvalidInputException(ServiceResultImp.error(translationAttribute, ErrorFactory.fromViolations(e.getConstraintViolations())));
        }
    }

    @Transactional
    public ServiceResult<TranslationAttribute> deleteSourceWord(DeleteSourceWordRequest request) {
        try {
            validate(request);
            Optional<Translation> optionalTranslation = translationRepository.deleteSourceWord(request);
            if(optionalTranslation.isPresent()) {
                TranslationAttribute translationAttribute = TranslationAttribute.builder()
                        .documentId(-1)
                        .selectedWordId(request.selectedWordId())
                        .translationId(-1)
                        .translation(optionalTranslation.get())
                        .currentFamiliarity(getFamiliarityAsDigit(optionalTranslation.get().familiarity()))
                        .familiarityLevels(getFamiliarityTable())
                        .isPhrase(request.isPhrase())
                        .build();
                return ServiceResultImp.success(translationAttribute);
            }
            throw new RuntimeException("Unknown exception");
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(ServiceResultImp.error(ErrorFactory.fromViolations(e.getConstraintViolations())));
        }
    }

    @Override
    public ServiceResult<Integer> getWordsLearnedCount(CustomUserDetails principal) {
        return ServiceResultImp.success(translationRepository.getWordsLearnedCount(principal.getUsername(), principal.targetLanguage()));
    }


    @Override
    public ServiceResult<Map<String, Translation>> findTranslationsByImport(FindTranslationsByImportRequest request) {
        Pattern newLinePattern = Pattern.compile("\n+");
        Pattern nonLetterNonNumberPattern = Pattern.compile("[^\\p{L}\\p{N}\\s-]");
        List<String> wordList = getContentAsWordList(request.anImport().pageContent(), Map.of("newLine", newLinePattern, "nonLetterNonNumber", nonLetterNonNumberPattern));
        System.out.println("searching db for: " + wordList);
        Map<String, Translation> translations = extractTranslationsFromDatabase(wordList, request.owner());
        System.out.println("db has: " + translations.values().stream().map(TranslationDetails::targetWord).toList());
        return ServiceResultImp.success(translations);
    }

    @Override
    public ServiceResult<List<Translation>> extractPhrases(String content, String owner) {
        List<Translation> phrases = translationRepository.extractPhrases(content, owner);
        return ServiceResultImp.success(phrases);
    }

    @Override
    public List<String> translate(TranslateRequest request) {

        List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(request.targetWord(), 3);
        return mergeSourceWordLists(
                sourceWordsFromDatabase,
                translationFetchingService.fetchTranslationsAsync(request.sourceLanguage(), request.targetLanguage(), request.targetWord())
        );
    }

    @Override
    public ServiceResult<BaseFlashcardAttribute> getRandomTranslations(GetRandomTranslationsRequest request) {
        List<Translation> translations = translationRepository.getRandomTranslations(request.isPhrase(), request.owner(), request.quantity(), request.familiarity());
        if(!translations.isEmpty()) {
            return ServiceResultImp.success(BaseFlashcardAttribute.builder()
                    .id(request.id())
                    .size(translations.size())
                    .familiarity(request.familiarity())
                    .quantity(request.quantity())
                    .isPhrase(request.isPhrase())
                    .translations(translations)
                    .build());
        }
        return ServiceResultImp.error(Map.of("emptyList", "No translations found to review"));

    }


    private List<String> getContentAsWordList(String content, Map<String, Pattern> patternMap) {
        return Arrays.stream(patternMap.get("newLine").matcher(content).replaceAll(" ")
                .split(" "))
                .map(word -> patternMap.get("nonLetterNonNumber").matcher(word).replaceAll("").replace("-", ""))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(word -> !word.isEmpty())
                .distinct()
                .toList();
    }

    private Optional<Translation> executeDatabaseUpdate(UpdateSourceWordsRequest request) {
        return translationRepository.updateSourceWords(request);
    }

    private Optional<Translation> getTranslationFromDatabase(FindByTargetWordRequest request) {
        return translationRepository.findByTargetWord(request);
    }

    private Map<String, Translation> extractTranslationsFromDatabase(List<String> wordList, String owner) {
        return translationRepository.findByTargetWords(wordList, owner).stream()
                .collect(Collectors.toUnmodifiableMap(
                                TranslationDetails::targetWord,
                                (value) -> value,
                                (first, second) -> first
                        )
                );
    }

    private Pattern compileNonSpecialChars() {
        return Pattern.compile("^[\\p{L}0-9 ]+$");
    }

    private List<String> mergeSourceWordLists(List<String> sourceWordsFromDatabase, List<String> sourceWordsFromDict) {
        return Stream.concat(sourceWordsFromDatabase.stream(), sourceWordsFromDict.stream())
                .filter(word -> !word.isBlank())
                .distinct()
                .toList();
    }

    private int insertIntoDatabase(Translation translation,  AddTranslationRequest request) {
        return translationRepository.addTranslation(translation, request.documentId()).orElseThrow(() -> new RuntimeException("Failed to add new translation"));
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
