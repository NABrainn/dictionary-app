package lule.dictionary.translations.service;

import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.familiarity.FamiliarityService;
import lule.dictionary.language.service.Language;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import lule.dictionary.translations.data.attribute.BaseFlashcardAttribute;
import lule.dictionary.translations.data.attribute.FlashcardConfigAttribute;
import lule.dictionary.translations.data.attribute.WordCardAttribute;
import lule.dictionary.translations.data.request.GetRandomTranslationsRequest;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.request.*;
import lule.dictionary.translations.service.exception.InvalidInputException;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.repository.TranslationRepository;
import lule.dictionary.translations.data.attribute.TranslationAttribute;
import lule.dictionary.translations.service.exception.TranslationContraintViolationException;
import lule.dictionary.translations.service.exception.TranslationsNotFoundException;
import lule.dictionary.translationFetching.service.TranslationFetchingExecutor;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.validation.service.ValidationService;
import lule.dictionary.validation.service.ValidationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationFetchingExecutor translationFetchingService;
    private final ValidationService validationService;
    private final FamiliarityService familiarityService;
    private final PatternService patternService;
    private final TranslationLocalizationService translationLocalization;

    @Transactional
    public TranslationAttribute createTranslation(@NonNull AddTranslationRequest request) throws InvalidInputException {
        try {
            validate(request);
            Translation translation = Translation.builder()
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
                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(familiarityService.getFamiliarityTable())
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .localization(translationLocalization.get(request.systemLanguage()))
                    .build();
        } catch (ValidationServiceException e) {
            TranslationAttribute translationAttribute = TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translation(null)
                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(familiarityService.getFamiliarityTable())
                    .translationId(-1)
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .localization(translationLocalization.get(request.systemLanguage()))
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
                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                    .familiarityLevels(familiarityService.getFamiliarityTable())
                    .translationId(-1)
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .localization(translationLocalization.get(request.systemLanguage()))
                    .build();
        }
        List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(request.targetWord(), 3);
        List<String> sourceWordsFromService = translationFetchingService.fetchTranslationsAsync(request.sourceLanguage(), request.targetLanguage(), request.targetWord());
        System.out.println(sourceWordsFromService);
        Translation translation = Translation.builder()
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
                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                .familiarityLevels(familiarityService.getFamiliarityTable())
                .translationId(-1)
                .documentId(request.documentId())
                .isPhrase(request.isPhrase())
                .localization(translationLocalization.get(request.systemLanguage()))
                .build();

    }

    @Transactional
    public TranslationAttribute updateFamiliarity(UpdateTranslationFamiliarityRequest request) {
        Translation translation = translationRepository.updateFamiliarity(request)
                .orElseThrow(() -> new RuntimeException("Failed to update familiarity for " + request.targetWord()));
        return TranslationAttribute.builder()
                .selectedWordId(request.selectedWordId())
                .translation(translation)
                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                .familiarityLevels(familiarityService.getFamiliarityTable())
                .translationId(-1)
                .documentId(-1)
                .isPhrase(request.isPhrase())
                .localization(translationLocalization.get(request.systemLanguage()))
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
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(optionalTranslation.get().familiarity()))
                        .familiarityLevels(familiarityService.getFamiliarityTable())
                        .isPhrase(request.isPhrase())
                        .localization(translationLocalization.get(request.systemLanguage()))
                        .build();
            }
            throw new RuntimeException("Unknown exception");
        } catch (ValidationServiceException e) {
            Translation translation = Translation.builder()
                    .sourceWords(request.sourceWords().stream()
                            .map(patternService::removeSpecialCharacters)
                            .filter(word -> !word.isBlank())
                            .toList())
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
                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                    .familiarityLevels(familiarityService.getFamiliarityTable())
                    .isPhrase(request.isPhrase())
                    .localization(translationLocalization.get(request.systemLanguage()))
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
                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(optionalTranslation.get().familiarity()))
                    .familiarityLevels(familiarityService.getFamiliarityTable())
                    .isPhrase(request.isPhrase())
                    .localization(translationLocalization.get(request.systemLanguage()))
                    .build();
        }
        throw new RuntimeException("Unknown exception");
    }

    public int getWordsLearnedCount(UserProfile principal) {
        return translationRepository.getWordsLearnedCount(principal.getUsername(), principal.targetLanguage());
    }

    public Map<String, Translation> findTranslationsInDocument(FindTranslationsInDocumentRequest request) {
        Pattern newLinePattern = Pattern.compile("\n+");
        Pattern nonLetterNonNumberPattern = Pattern.compile("[^\\p{L}\\p{N}\\s-]");
        List<String> wordList = getContentAsWordList(request.contentBlob(), Map.of("newLine", newLinePattern, "nonLetterNonNumber", nonLetterNonNumberPattern));
        return extractTranslationsFromDatabase(wordList, request.owner());
    }

    public List<Translation> extractPhrases(ExtractPhrasesRequest request) {
        return translationRepository.extractPhrases(request.content(), request.owner()).stream()
                .distinct()
                .toList();
    }

    public TranslationAttribute translate(CreateTranslationRequest request) {
        List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(patternService.removeSpecialCharacters(request.targetWord()).toLowerCase(), 3);
        List<String> sourceWordsFromService = translationFetchingService.fetchTranslationsAsync(request.sourceLanguage(), request.targetLanguage(), request.targetWord());
        List<String> sourceWords = Stream.concat(sourceWordsFromDatabase.stream(), sourceWordsFromService.stream())
                .filter(word -> !word.isBlank())
                .distinct()
                .limit(3)
                .toList();
        return TranslationAttribute.builder()
                .documentId(request.documentId())
                .selectedWordId(request.selectedWordId())
                .translationId(-1)
                .translation(Translation.builder()
                        .sourceWords(sourceWords)
                        .targetWord(request.targetWord())
                        .familiarity(Familiarity.UNKNOWN)
                        .sourceLanguage(request.sourceLanguage())
                        .targetLanguage(request.targetLanguage())
                        .owner(request.username())
                        .isPhrase(request.isPhrase())
                        .build())
                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(Familiarity.UNKNOWN))
                .isPhrase(request.isPhrase())
                .familiarityLevels(familiarityService.getFamiliarityTable())
                .localization(translationLocalization.get(request.systemLanguage()))
                .build();
    }

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
                    .localization(translationLocalization.get(request.systemLanguage()))
                    .build();
        }
        throw new TranslationsNotFoundException("No translations found to review", FlashcardConfigAttribute.builder()
                .familiarity(request.familiarity())
                .quantity(request.quantity())
                .isPhrase(request.isPhrase())
                .localization(translationLocalization.get(request.systemLanguage()))
                .build());

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
                .distinct()
                .collect(Collectors.toUnmodifiableMap(Translation::targetWord, value -> value));
    }

    private int insertIntoDatabase(Translation translation,  AddTranslationRequest request) {
        return translationRepository.addTranslation(translation, request.documentId()).orElseThrow(() -> new RuntimeException("Failed to add new translation"));
    }

    private void validate(TranslationsRequest request) throws ConstraintViolationException {
        validationService.validate(request);
    }

    public Map<TranslationLocalizationKey, String> getFlashcardLocalization(Language language) {
        return translationLocalization.get(language);
    }

    public FlashcardConfigAttribute getFlashcardConfig(ConfigureFlashcardRequest request) {
        return FlashcardConfigAttribute.builder()
                .familiarity(request.familiarity())
                .quantity(request.quantity())
                .isPhrase(request.isPhrase())
                .localization(translationLocalization.get(request.systemLanguage()))
                .build();
    }

    public BaseFlashcardAttribute flipFlashcard(FlipFlashcardRequest request) {
        return BaseFlashcardAttribute.builder()
                .translations(request.translations())
                .localization(translationLocalization.get(request.systemLanguage()))
                .id(request.id())
                .size(request.size())
                .familiarity(request.familiarity())
                .quantity(request.quantity())
                .isPhrase(request.isPhrase())
                .build();
    }

    public WordCardAttribute getCardAttribute(GetCardAttributeRequest request) {
        return WordCardAttribute.of(request.sourceWord(), request.targetWord());
    }
}
