package lule.dictionary.translations.service;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.translations.controller.vocabulary.dto.BaseFlashcardAttribute;
import lule.dictionary.translations.controller.vocabulary.dto.GetRandomTranslationsRequest;
import lule.dictionary.translations.data.TranslationImp;
import lule.dictionary.translations.data.request.*;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.translations.service.exception.InvalidInputException;
import lule.dictionary.shared.ServiceRequest;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.repository.TranslationRepository;
import lule.dictionary.errorLocalization.service.ErrorLocalizationImp;
import lule.dictionary.translations.data.attribute.TranslationAttribute;
import lule.dictionary.translations.service.exception.TranslationContraintViolationException;
import lule.dictionary.translations.service.exception.TranslationsNotFoundException;
import lule.dictionary.translationFetching.service.TranslationFetchingExecutor;
import lule.dictionary.validation.service.ValidationServiceException;
import lule.dictionary.validation.service.ValidationService;
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
    private final TranslationFetchingExecutor translationFetchingService;
    private final ValidationService validationService;
    private final ErrorLocalizationImp errorLocalization;

    @Transactional
    public TranslationAttribute createTranslation(@NonNull AddTranslationRequest request) throws InvalidInputException {
        try {
            System.out.println(request);
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
            return TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translationId(translationId)
                    .translation(translation)
                    .currentFamiliarity(getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .build();
        } catch (ValidationServiceException e) {
            TranslationAttribute translationAttribute = TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translation(null)
                    .currentFamiliarity(getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .translationId(-1)
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .build();
            throw new TranslationContraintViolationException(translationAttribute, e.getViolation());
        }
    }

    @Transactional
    public TranslationAttribute findByTargetWord(@NonNull FindByTargetWordRequest request)throws InvalidInputException {
        validate(request);
        Optional<Translation> optionalTranslation = getTranslationFromDatabase(request);
        if(optionalTranslation.isPresent()) {
            Translation translation = optionalTranslation.get();
            return TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translation(translation)
                    .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .translationId(-1)
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .build();
        }
        List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(request.targetWord(), 3);
        List<String> sourceWordsFromService = translationFetchingService.fetchTranslationsAsync(request.sourceLanguage(), request.targetLanguage(), request.targetWord());
        System.out.println(sourceWordsFromService);
        Translation translation = TranslationImp.builder()
                .sourceWords(Stream.concat(sourceWordsFromDatabase.stream(), sourceWordsFromService.stream())
                        .filter(word -> !word.isBlank())
                        .distinct()
                        .limit(3)
                        .toList())
                .targetWord(request.targetWord())
                .familiarity(Familiarity.UNKNOWN)
                .sourceLanguage(request.sourceLanguage())
                .targetLanguage(request.targetLanguage())
                .owner(request.owner())
                .isPhrase(request.isPhrase())
                .build();
        return TranslationAttribute.builder()
                .selectedWordId(request.selectedWordId())
                .translation(translation)
                .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                .familiarityLevels(getFamiliarityTable())
                .translationId(-1)
                .documentId(request.documentId())
                .isPhrase(request.isPhrase())
                .build();

    }

    @Transactional
    public TranslationAttribute updateFamiliarity(UpdateTranslationFamiliarityRequest request) {
        Translation translation = translationRepository.updateFamiliarity(request)
                .orElseThrow(() -> new RuntimeException("Failed to update familiarity for " + request.targetWord()));
        return TranslationAttribute.builder()
                .selectedWordId(request.selectedWordId())
                .translation(translation)
                .currentFamiliarity(getFamiliarityAsDigit(translation.familiarity()))
                .familiarityLevels(getFamiliarityTable())
                .translationId(-1)
                .documentId(-1)
                .isPhrase(request.isPhrase())
                .build();
    }

    @Transactional
    public TranslationAttribute updateSourceWords(UpdateSourceWordsRequest request) throws InvalidInputException {
        try {
            validate(request);
            Optional<Translation> optionalTranslation = executeDatabaseUpdate(request);
            if(optionalTranslation.isPresent()) {
                return TranslationAttribute.builder()
                        .documentId(-1)
                        .selectedWordId(request.selectedWordId())
                        .translationId(-1)
                        .translation(optionalTranslation.get())
                        .currentFamiliarity(getFamiliarityAsDigit(optionalTranslation.get().familiarity()))
                        .familiarityLevels(getFamiliarityTable())
                        .isPhrase(request.isPhrase())
                        .build();
            }
            throw new RuntimeException("Unknown exception");
        } catch (ValidationServiceException e) {
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
            throw new TranslationContraintViolationException(translationAttribute, e.getViolation());
        }
    }

    @Transactional
    public TranslationAttribute deleteSourceWord(DeleteSourceWordRequest request) {
        validate(request);
        Optional<Translation> optionalTranslation = translationRepository.deleteSourceWord(request);
        if(optionalTranslation.isPresent()) {
            return TranslationAttribute.builder()
                    .documentId(-1)
                    .selectedWordId(request.selectedWordId())
                    .translationId(-1)
                    .translation(optionalTranslation.get())
                    .currentFamiliarity(getFamiliarityAsDigit(optionalTranslation.get().familiarity()))
                    .familiarityLevels(getFamiliarityTable())
                    .isPhrase(request.isPhrase())
                    .build();
        }
        throw new RuntimeException("Unknown exception");
    }

    @Override
    public int getWordsLearnedCount(UserProfile principal) {
        return translationRepository.getWordsLearnedCount(principal.getUsername(), principal.targetLanguage());
    }


    @Override
    public Map<String, Translation> findTranslationsByImport(FindTranslationsByImportRequest request) {
        Pattern newLinePattern = Pattern.compile("\n+");
        Pattern nonLetterNonNumberPattern = Pattern.compile("[^\\p{L}\\p{N}\\s-]");
        List<String> wordList = getContentAsWordList(request.anImport().pageContent(), Map.of("newLine", newLinePattern, "nonLetterNonNumber", nonLetterNonNumberPattern));
        return extractTranslationsFromDatabase(wordList, request.owner());
    }

    @Override
    public List<Translation> extractPhrases(String content, String owner) {
        return translationRepository.extractPhrases(content, owner).stream()
                .distinct()
                .toList();
    }

    @Override
    public List<String> translate(TranslateRequest request) {
        List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(request.targetWord().replaceAll("[^\\p{L}\\p{N}\\s-]", "").toLowerCase(), 3);
        List<String> sourceWordsFromService = translationFetchingService.fetchTranslationsAsync(request.sourceLanguage(), request.targetLanguage(), request.targetWord());
        return Stream.concat(sourceWordsFromDatabase.stream(), sourceWordsFromService.stream())
                .filter(word -> !word.isBlank())
                .distinct()
                .limit(3)
                .toList();
    }

    @Override
    public BaseFlashcardAttribute getRandomTranslations(GetRandomTranslationsRequest request) throws TranslationsNotFoundException {
        List<Translation> translations = translationRepository.getRandomTranslations(request.isPhrase(), request.owner(), request.quantity(), request.familiarity());
        if(!translations.isEmpty()) {
            return BaseFlashcardAttribute.builder()
                    .id(request.id())
                    .size(translations.size())
                    .familiarity(request.familiarity())
                    .quantity(request.quantity())
                    .isPhrase(request.isPhrase())
                    .translations(translations)
                    .build();
        }
        throw new TranslationsNotFoundException("No translations found to review");

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
                .collect(Collectors.toUnmodifiableMap(Translation::targetWord, value -> value));
    }

    private Pattern compileNonSpecialChars() {
        return Pattern.compile("^[\\p{L}0-9 ]+$");
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
