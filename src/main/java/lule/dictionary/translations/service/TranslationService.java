package lule.dictionary.translations.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.familiarity.service.FamiliarityService;
import lule.dictionary.language.service.Language;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.stringUtil.service.StringUtils;
import lule.dictionary.translations.data.TranslationFormType;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import lule.dictionary.translations.data.attribute.*;
import lule.dictionary.translations.data.entity.*;
import lule.dictionary.translations.data.request.GetRandomTranslationsRequest;
import lule.dictionary.translations.data.request.*;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.repository.TranslationRepository;
import lule.dictionary.translations.data.exception.TranslationServiceException;
import lule.dictionary.translations.data.exception.TranslationsNotFoundException;
import lule.dictionary.translationFetching.service.TranslationFetcherManager;
import lule.dictionary.userProfiles.data.OwnerInfo;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.ValidationException;
import lule.dictionary.validation.data.rule.NotEmpty;
import lule.dictionary.validation.data.rule.Size;
import lule.dictionary.validation.service.Validator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationFetcherManager translationFetchingService;
    private final Validator validator;
    private final FamiliarityService familiarityService;
    private final PatternService patternService;
    private final TranslationLocalizationService translationLocalization;
    private final StringUtils stringUtils;

    @Transactional
    public Result<TranslationAttribute> createTranslation(@NonNull AddTranslationRequest request,
                                                  @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        String sanitizedSourceWord = request.sourceWords().stream()
                .filter(Predicate.not(String::isBlank))
                .findFirst()
                .map(String::trim)
                .map(patternService::removeSpecialCharacters)
                .orElse("");
        String sanitizedTargetWord = patternService.removeSpecialCharacters(request.targetWord().toLowerCase().trim());
        Result<?> result = request.isPhrase() ?
                validator.validate(
                        Constraint.of("sourceWord", Size.of(sanitizedSourceWord, 0, 250), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być dłuższe niż 250 znaków";
                            case EN -> "Source word cannot be longer than 250 characters";
                            case IT -> "La parola sorgente non può superare i 250 caratteri";
                            case NO -> "Kildeordet kan ikke være lengre enn 250 tegn";
                        }),
                        Constraint.of("processedTargetWord", Size.of(sanitizedTargetWord, 0, 250), switch (uiLanguage) {
                            case PL -> "Słowo docelowe nie może być dłuższe niż 250 znaków";
                            case EN -> "Target word cannot be longer than 250 characters";
                            case IT -> "La parola di destinazione non può superare i 250 caratteri";
                            case NO -> "Målordet kan ikke være lengre enn 250 tegn";
                        }),
                        Constraint.of("sourceWord", NotEmpty.of(sanitizedSourceWord), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być puste";
                            case EN -> "Source word cannot be empty";
                            case IT -> "La parola sorgente non può essere vuota";
                            case NO -> "Kildeordet kan ikke være tomt";
                        })
                ) :
                validator.validate(
                        Constraint.of("sourceWord", Size.of(sanitizedSourceWord, 0, 50), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być dłuższe niż 50 znaków";
                            case EN -> "Source word cannot be longer than 50 characters";
                            case IT -> "La parola sorgente non può superare i 50 caratteri";
                            case NO -> "Kildeordet kan ikke være lengre enn 50 tegn";
                        }),
                        Constraint.of("processedTargetWord", Size.of(sanitizedTargetWord, 0, 50), switch (uiLanguage) {
                            case PL -> "Słowo docelowe nie może być dłuższe niż 50 znaków";
                            case EN -> "Target word cannot be longer than 50 characters";
                            case IT -> "La parola di destinazione non può superare i 50 caratteri";
                            case NO -> "Målordet kan ikke være lengre enn 50 tegn";
                        }),
                        Constraint.of("sourceWord", NotEmpty.of(sanitizedSourceWord), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być puste";
                            case EN -> "Source word cannot be empty";
                            case IT -> "La parola sorgente non può essere vuota";
                            case NO -> "Kildeordet kan ikke być tomt";
                        })
                );
        return switch (result) {
            case Ok<?> ignored -> {
                Translation translation = request.isPhrase() ?
                        Phrase.of(request.sourceWords(), request.targetWord(), request.familiarity(), OwnerInfo.of(request.sourceLanguage(), request.targetLanguage(), principal.getUsername())) :
                        Word.of(request.sourceWords(), request.targetWord(), request.familiarity(), OwnerInfo.of(request.sourceLanguage(), request.targetLanguage(), principal.getUsername()));
                translationRepository.addTranslation(translation)
                        .orElseThrow();
                TranslationAttribute attribute = TranslationAttribute.builder()
                        .id(request.selectedWordId())
                        .translation(translation.withSourceWords(translation.sourceWords().stream()
                                .filter(Predicate.not(String::isBlank))
                                .distinct()
                                .limit(3)
                                .toList()))
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(request.familiarity()))
                        .familiarityLevels(familiarityService.getFamiliarityMap())
                        .documentId(request.documentId())
                        .isPhrase(request.isPhrase())
                        .isPersisted(true)
                        .build();
                yield Ok.of(attribute);
            }
            case Err<?> err -> {
                Translation translation = request.isPhrase() ?
                        Phrase.of(List.of(), request.targetWord(), request.familiarity(), OwnerInfo.of(request.sourceLanguage(), request.targetLanguage(), principal.username())) :
                        Word.of(List.of(), request.targetWord(), request.familiarity(), OwnerInfo.of(request.sourceLanguage(), request.targetLanguage(), principal.username()));
                TranslationAttribute translationAttribute = TranslationAttribute.builder()
                        .id(request.selectedWordId())
                        .translation(translation)
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(request.familiarity()))
                        .familiarityLevels(familiarityService.getFamiliarityMap())
                        .documentId(request.documentId())
                        .isPhrase(request.isPhrase())
                        .isPersisted(false)
                        .build();
                yield err.throwable() instanceof ValidationException validationException ?
                        Err.of(new TranslationServiceException(translationAttribute, validationException.getViolations())) :
                        Err.of(new RuntimeException("Unknown exception"));
            }
        };
    }

    @Transactional
    public TranslationAttribute updateFamiliarity(@NonNull UpdateTranslationFamiliarityRequest request,
                                                  @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Translation translation = translationRepository.updateFamiliarity(request.familiarity(), request.targetWord(), principal.username())
                .orElseThrow(() -> new RuntimeException("Failed to update familiarity for " + request.targetWord()));
        return TranslationAttribute.builder()
                .id(request.selectedWordId())
                .translation(translation.withSourceWords(translation.sourceWords().stream()
                        .filter(Predicate.not(String::isBlank))
                        .distinct()
                        .limit(3)
                        .toList()))
                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                .familiarityLevels(familiarityService.getFamiliarityMap())
                .documentId(-1)
                .isPhrase(request.isPhrase())
                .isPersisted(true)
                .build();
    }

    @Transactional
    public Result<TranslationAttribute> updateSourceWords(@NonNull UpdateSourceWordsRequest request,
                                                  @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();

        String sanitizedSourceWord = !request.sourceWords().isEmpty() ?
                Optional.of(request.sourceWords().getLast())
                .map(patternService::removeSpecialCharacters)
                .map(String::trim)
                .orElse("") :
                "";
        String sanitizedTargetWord = patternService.removeSpecialCharacters(request.targetWord());
        Result<?> result = request.isPhrase() ?
                validator.validate(
                        Constraint.of("sourceWord", Size.of(sanitizedSourceWord, 0, 250), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być dłuższe niż 250 znaków";
                            case EN -> "Source word cannot be longer than 250 characters";
                            case IT -> "La parola sorgente non può superare i 250 caratteri";
                            case NO -> "Kildeordet kan ikke være lengre enn 250 tegn";
                        }),
                        Constraint.of("processedTargetWord", Size.of(sanitizedTargetWord, 0, 250), switch (uiLanguage) {
                            case PL -> "Słowo docelowe nie może być dłuższe niż 250 znaków";
                            case EN -> "Target word cannot be longer than 250 characters";
                            case IT -> "La parola di destinazione non può superare i 250 caratteri";
                            case NO -> "Målordet kan ikke være lengre enn 250 tegn";
                        }),
                        Constraint.of("sourceWord", NotEmpty.of(sanitizedSourceWord), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być puste";
                            case EN -> "Source word cannot be empty";
                            case IT -> "La parola sorgente non può essere vuota";
                            case NO -> "Kildeordet kan ikke være tomt";
                        })
                ) :
                validator.validate(
                        Constraint.of("sourceWord", Size.of(sanitizedSourceWord, 0, 50), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być dłuższe niż 50 znaków";
                            case EN -> "Source word cannot be longer than 50 characters";
                            case IT -> "La parola sorgente non può superare i 50 caratteri";
                            case NO -> "Kildeordet kan ikke være lengre enn 50 tegn";
                        }),
                        Constraint.of("processedTargetWord", Size.of(sanitizedTargetWord, 0, 50), switch (uiLanguage) {
                            case PL -> "Słowo docelowe nie może być dłuższe niż 50 znaków";
                            case EN -> "Target word cannot be longer than 50 characters";
                            case IT -> "La parola di destinazione non può superare i 50 caratteri";
                            case NO -> "Målordet kan ikke være lengre enn 50 tegn";
                        }),
                        Constraint.of("sourceWord", NotEmpty.of(sanitizedSourceWord), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być puste";
                            case EN -> "Source word cannot be empty";
                            case IT -> "La parola sorgente non può essere vuota";
                            case NO -> "Kildeordet kan ikke być tomt";
                        })
                );
        return switch (result) {
            case Ok<?> ignored -> translationRepository.updateSourceWords(request.sourceWords(), request.targetWord(), principal.username())
                    .map(translation -> TranslationAttribute.builder()
                            .documentId(-1)
                            .id(request.selectedWordId())
                            .translation(translation.withSourceWords(translation.sourceWords().stream()
                                    .filter(word -> !word.isBlank())
                                    .distinct()
                                    .limit(3)
                                    .toList()))
                            .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                            .familiarityLevels(familiarityService.getFamiliarityMap())
                            .isPhrase(request.isPhrase())
                            .isPersisted(true)
                            .build())
                    .map(Ok::of)
                    .orElseThrow();
            case Err<?> err -> {
                Translation translation = request.isPhrase() ?
                        Phrase.of(List.of(), request.targetWord(), request.familiarity(), OwnerInfo.of(principal.sourceLanguage(), principal.targetLanguage(), principal.username())) :
                        Word.of(List.of(), request.targetWord(), request.familiarity(), OwnerInfo.of(principal.sourceLanguage(), principal.targetLanguage(), principal.username()));
                TranslationAttribute translationAttribute = TranslationAttribute.builder()
                        .documentId(-1)
                        .id(request.selectedWordId())
                        .translation(translation.withSourceWords(translation.sourceWords().stream()
                                .filter(word -> !word.isBlank())
                                .distinct()
                                .limit(3)
                                .toList()))
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                        .familiarityLevels(familiarityService.getFamiliarityMap())
                        .isPhrase(request.isPhrase())
                        .isPersisted(true)
                        .build();
                yield err.throwable() instanceof ValidationException exception
                        ? Err.of(new TranslationServiceException(translationAttribute, exception.getViolations()))
                        : Err.of(new RuntimeException("Unknown exception", err.throwable()));
            }
        };
    }

    @Transactional
    public TranslationAttribute deleteSourceWord(DeleteSourceWordRequest request, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        String sanitizedSourceWord = patternService.removeSpecialCharacters(request.sourceWord());
        String sanitizedTargetWord = patternService.removeSpecialCharacters(request.targetWord());
        Result<?> result = request.isPhrase() ?
                validator.validate(
                        Constraint.of("sourceWord", Size.of(sanitizedSourceWord, 0, 250), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być dłuższe niż 250 znaków";
                            case EN -> "Source word cannot be longer than 250 characters";
                            case IT -> "La parola sorgente non può superare i 250 caratteri";
                            case NO -> "Kildeordet kan ikke være lengre enn 250 tegn";
                        }),
                        Constraint.of("processedTargetWord", Size.of(sanitizedTargetWord, 0, 250), switch (uiLanguage) {
                            case PL -> "Słowo docelowe nie może być dłuższe niż 250 znaków";
                            case EN -> "Target word cannot be longer than 250 characters";
                            case IT -> "La parola di destinazione non può superare i 250 caratteri";
                            case NO -> "Målordet kan ikke være lengre enn 250 tegn";
                        }),
                        Constraint.of("sourceWord", NotEmpty.of(sanitizedSourceWord), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być puste";
                            case EN -> "Source word cannot be empty";
                            case IT -> "La parola sorgente non può essere vuota";
                            case NO -> "Kildeordet kan ikke være tomt";
                        })
                ) :
                validator.validate(
                        Constraint.of("sourceWord", Size.of(sanitizedSourceWord, 0, 50), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być dłuższe niż 50 znaków";
                            case EN -> "Source word cannot be longer than 50 characters";
                            case IT -> "La parola sorgente non può superare i 50 caratteri";
                            case NO -> "Kildeordet kan ikke være lengre enn 50 tegn";
                        }),
                        Constraint.of("processedTargetWord", Size.of(sanitizedTargetWord, 0, 50), switch (uiLanguage) {
                            case PL -> "Słowo docelowe nie może być dłuższe niż 50 znaków";
                            case EN -> "Target word cannot be longer than 50 characters";
                            case IT -> "La parola di destinazione non può superare i 50 caratteri";
                            case NO -> "Målordet kan ikke være lengre enn 50 tegn";
                        }),
                        Constraint.of("sourceWord", NotEmpty.of(sanitizedSourceWord), switch (uiLanguage) {
                            case PL -> "Słowo źródłowe nie może być puste";
                            case EN -> "Source word cannot be empty";
                            case IT -> "La parola sorgente non può essere vuota";
                            case NO -> "Kildeordet kan ikke być tomt";
                        })
                );
        return translationRepository.deleteSourceWord(request.sourceWord(), request.targetWord(), principal.username())
                .map(translation -> TranslationAttribute.builder()
                        .documentId(-1)
                        .id(request.selectedWordId())
                        .translation(translation.withSourceWords(translation.sourceWords().stream()
                                .filter(word -> !word.isBlank())
                                .distinct()
                                .limit(3)
                                .toList()))
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                        .familiarityLevels(familiarityService.getFamiliarityMap())
                        .isPhrase(request.isPhrase())
                        .isPersisted(true)
                        .build())
                .orElseThrow();
    }

    public int getWordsLearnedCount(UserProfile principal) {
        return translationRepository.getWordsLearnedCount(principal.getUsername(), principal.targetLanguage());
    }

    public Map<String, Translation> findTranslations(FindTranslationsInDocumentRequest request) {
        List<String> wordList = Arrays.stream(patternService.replaceNewlinesWithSpaces(request.contentBlob()).split(" "))
                .map(patternService::removeSpecialCharacters)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(word -> !word.isEmpty())
                .distinct()
                .toList();
        return translationRepository.findByTargetWords(wordList, request.owner()).stream()
                .distinct()
                .collect(Collectors.toUnmodifiableMap(Translation::processedTargetWord, value -> value));
    }

    public List<Translation> findPhrases(ExtractPhrasesRequest request) {
        return translationRepository.findPhrases(request.content(), request.owner());
    }

    public TranslationAttribute translate(CreateTranslationFormRequest request, @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        String sanitizedTargetWord = patternService.removeSpecialCharacters(request.unprocessedTargetWord())
                .trim()
                .toLowerCase();
        List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(patternService.removeSpecialCharacters(sanitizedTargetWord).toLowerCase(), 3);
        List<String> sourceWordsFromService = translationFetchingService.fetchTranslationsAsync(principal.sourceLanguage(), principal.targetLanguage(), sanitizedTargetWord);
        List<String> sourceWords = Stream.concat(sourceWordsFromDatabase.stream(), sourceWordsFromService.stream())
                .filter(word -> !word.isBlank())
                .distinct()
                .limit(3)
                .toList();
        Translation uninitializedTranslation = request.isPhrase() ?
                UninitializedPhrase.of(sourceWords, sanitizedTargetWord, OwnerInfo.of(principal.sourceLanguage(), principal.targetLanguage(), principal.username())) :
                UninitializedWord.of(sourceWords, sanitizedTargetWord, OwnerInfo.of(principal.sourceLanguage(), principal.targetLanguage(), principal.username()));
        return TranslationAttribute.builder()
                .documentId(request.documentId())
                .id(request.selectedWordId())
                .translation(uninitializedTranslation)
                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(Familiarity.UNKNOWN))
                .isPhrase(request.isPhrase())
                .familiarityLevels(familiarityService.getFamiliarityMap())
                .type(TranslationFormType.CREATE)
                .isPersisted(false)
                .build();
    }

    public Result<BaseFlashcardAttribute> startFlashcardSession(GetRandomTranslationsRequest request, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        List<Translation> translations = translationRepository.startFlashcardSession(request.isPhrase(), principal.getUsername(), request.quantity(), request.familiarity());
        return !translations.isEmpty() ?
                Ok.of(BaseFlashcardAttribute.builder()
                    .id(request.id())
                    .size(translations.size())
                    .familiarity(request.familiarity())
                    .quantity(request.quantity())
                    .isPhrase(request.isPhrase())
                    .translations(translations)
                    .localization(translationLocalization.translationFormMessages(principal.userInterfaceLanguage()))
                    .build()) :
                Err.of(new TranslationsNotFoundException("No translations found to review", FlashcardConfigAttribute.builder()
                        .familiarity(request.familiarity())
                        .quantity(request.quantity())
                        .isPhrase(request.isPhrase())
                        .build()));


    }

    public FlashcardConfigAttribute getFlashcardConfig(ConfigureFlashcardRequest request) {
        return FlashcardConfigAttribute.builder()
                .familiarity(request.familiarity())
                .quantity(request.quantity())
                .isPhrase(request.isPhrase())
                .build();
    }

    public BaseFlashcardAttribute flipFlashcard(@NonNull FlipFlashcardRequest request,
                                                @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        List<Translation> translations = translationRepository.getTranslationsFromSession(principal.getUsername());
        return BaseFlashcardAttribute.builder()
                .translations(translations)
                .localization(translationLocalization.translationFormMessages(principal.userInterfaceLanguage()))
                .id(request.id())
                .size(translations.size())
                .familiarity(request.familiarity())
                .quantity(request.quantity())
                .isPhrase(request.isPhrase())
                .build();
    }

    public WordCardAttribute getCardAttribute(GetCardAttributeRequest request) {
        return WordCardAttribute.of(request.sourceWord(), request.targetWord());
    }

    public Result<TranslationAttribute> findOrCreateTranslation(GetTranslationFormRequest request, Authentication authentication) {
        return switch (request) {
            case CreateTranslationFormRequest createTranslationRequest -> {
                UserProfile principal = (UserProfile) authentication.getPrincipal();
                String sanitizedTargetWord = patternService.removeSpecialCharacters(createTranslationRequest.unprocessedTargetWord())
                        .trim()
                        .toLowerCase();
                List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(sanitizedTargetWord, 3);
                List<String> sourceWordsFromService = translationFetchingService.fetchTranslationsAsync(principal.sourceLanguage(), principal.targetLanguage(), sanitizedTargetWord);
                List<String> sourceWords = Stream.concat(sourceWordsFromDatabase.stream(), sourceWordsFromService.stream())
                        .filter(Predicate.not(String::isBlank))
                        .distinct()
                        .limit(3)
                        .toList();
                Translation uninitializedTranslation = createTranslationRequest.isPhrase() ?
                        UninitializedPhrase.of(sourceWords, sanitizedTargetWord, OwnerInfo.of(principal.sourceLanguage(), principal.targetLanguage(), principal.username())) :
                        UninitializedWord.of(sourceWords, sanitizedTargetWord, OwnerInfo.of(principal.sourceLanguage(), principal.targetLanguage(), principal.username()));
                yield Ok.of(TranslationAttribute.builder()
                        .documentId(createTranslationRequest.documentId())
                        .id(createTranslationRequest.selectedWordId())
                        .translation(uninitializedTranslation)
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(Familiarity.UNKNOWN))
                        .isPhrase(createTranslationRequest.isPhrase())
                        .familiarityLevels(familiarityService.getFamiliarityMap())
                        .type(TranslationFormType.CREATE)
                        .isPersisted(false)
                        .build());
            }
            case FindTranslationFormRequest findTranslationRequest -> {
                UserProfile principal = (UserProfile) authentication.getPrincipal();
                String sanitizedTargetWord = patternService.removeSpecialCharacters(findTranslationRequest.unprocessedTargetWord())
                        .trim()
                        .toLowerCase();
                Result<?> result = findTranslationRequest.isPhrase() ?
                        validator.validate(
                                Constraint.of("processedTargetWord", Size.of(sanitizedTargetWord, 0, 250), switch (principal.userInterfaceLanguage()) {
                                    case PL -> "Docelowe słowo nie może mieć więcej niż 250 znaków";
                                    case EN -> "Target word cannot exceed 250 characters";
                                    case IT -> "La parola target non può superare i 250 caratteri";
                                    case NO -> "Målordet kan ikke være lenger enn 250 tegn";
                                })
                        ) :
                        validator.validate(
                                Constraint.of("processedTargetWord", Size.of(sanitizedTargetWord, 0, 50), switch (principal.userInterfaceLanguage()) {
                                    case PL -> "Docelowe słowo nie może mieć więcej niż 50 znaków";
                                    case EN -> "Target word cannot exceed 50 characters";
                                    case IT -> "La parola target non può superare i 50 caratteri";
                                    case NO -> "Målordet kan ikke være lenger enn 50 tegn";
                                })
                        );

                yield switch (result) {
                    case Ok<?> ignored -> translationRepository.findByTargetWord(sanitizedTargetWord, principal.username())
                            .map(translation -> TranslationAttribute.builder()
                                    .id(findTranslationRequest.selectedWordId())
                                    .translation(translation.withSourceWords(translation.sourceWords()))
                                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                                    .familiarityLevels(familiarityService.getFamiliarityMap())
                                    .documentId(findTranslationRequest.documentId())
                                    .isPhrase(findTranslationRequest.isPhrase())
                                    .type(TranslationFormType.FIND)
                                    .isPersisted(true)
                                    .build())
                            .map(Ok::of)
                            .orElseGet(() ->
                                    Stream.of(
                                            translationRepository.findMostFrequentSourceWords(sanitizedTargetWord, 3),
                                            translationFetchingService.fetchTranslationsAsync(principal.sourceLanguage(), principal.targetLanguage(), sanitizedTargetWord)
                                    )
                                    .map(fetchedSourceWords ->  findTranslationRequest.isPhrase() ?
                                            UninitializedPhrase.of(
                                                    fetchedSourceWords,
                                                    sanitizedTargetWord,
                                                    OwnerInfo.of(principal.sourceLanguage(), principal.targetLanguage(), principal.username())
                                            ) :
                                            UninitializedWord.of(
                                                    fetchedSourceWords,
                                                    sanitizedTargetWord,
                                                    OwnerInfo.of(principal.sourceLanguage(), principal.targetLanguage(), principal.username()))
                                    )
                                    .map(translation -> TranslationAttribute.builder()
                                            .id(findTranslationRequest.selectedWordId())
                                            .translation(translation)
                                            .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                                            .familiarityLevels(familiarityService.getFamiliarityMap())
                                            .documentId(findTranslationRequest.documentId())
                                            .isPhrase(findTranslationRequest.isPhrase())
                                            .type(TranslationFormType.FIND)
                                            .isPersisted(false)
                                            .build())
                                    .map(Ok::of)
                                    .findFirst()
                                    .get());
                    case Err<?> err -> throw new RuntimeException(err.throwable());
                };
            }
        };
    }

    public Map<TranslationLocalizationKey, String> getTranslationFormMessages(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return translationLocalization.translationFormMessages(principal.userInterfaceLanguage());
    }

    public Map<TranslationLocalizationKey, String> getVocabularyMessages(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return translationLocalization.vocabularyMessages(principal.userInterfaceLanguage());
    }
}
