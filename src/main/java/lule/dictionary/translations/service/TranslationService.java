package lule.dictionary.translations.service;

import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.familiarity.service.FamiliarityService;
import lule.dictionary.language.service.Language;
import lule.dictionary.session.service.SessionHelper;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.translations.data.TranslationFormType;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import lule.dictionary.translations.data.attribute.*;
import lule.dictionary.translations.data.request.GetRandomTranslationsRequest;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.translations.data.request.*;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.translations.data.repository.TranslationRepository;
import lule.dictionary.translations.data.exception.TranslationServiceException;
import lule.dictionary.translations.data.exception.TranslationsNotFoundException;
import lule.dictionary.translationFetching.service.TranslationFetchingExecutor;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.ValidationException;
import lule.dictionary.validation.service.Validator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationFetchingExecutor translationFetchingService;
    private final Validator validator;
    private final FamiliarityService familiarityService;
    private final PatternService patternService;
    private final TranslationLocalizationService translationLocalization;
    private final SessionHelper sessionHelper;

    @Transactional
    public TranslationAttribute createTranslation(@NonNull AddTranslationRequest request,
                                                  @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        try {
            String sanitizedSourceWord = request.sourceWords().stream()
                    .findFirst()
                    .map(String::trim)
                    .map(patternService::removeSpecialCharacters)
                    .orElse("");
            validator.validate(List.of(
                    Constraint.define("sourceWord", () -> sanitizedSourceWord.length() > 150, switch(uiLanguage) {
                        case PL -> "Słowo źródłowe nie może być dłuższe niż 150 znaków";
                        case EN -> "Source word cannot be longer than 150 characters";
                        case IT -> "La parola sorgente non può superare i 150 caratteri";
                        case NO -> "Kildeordet kan ikke være lengre enn 150 tegn";
                    }),
                    Constraint.define("sourceWord", sanitizedSourceWord::isBlank, switch(uiLanguage) {
                        case PL -> "Słowo źródłowe nie może być puste";
                        case EN -> "Source word cannot be empty";
                        case IT -> "La parola sorgente non può essere vuota";
                        case NO -> "Kildeordet kan ikke være tomt";
                    })
            ));
            Translation translation = Translation.builder()
                    .sourceWords(request.sourceWords())
                    .targetWord(request.targetWord())
                    .familiarity(request.familiarity())
                    .sourceLanguage(request.sourceLanguage())
                    .targetLanguage(request.targetLanguage())
                    .owner(principal.getUsername())
                    .isPhrase(request.isPhrase())
                    .unprocessedTargetWord("")
                    .build();
            int translationId = translationRepository.addTranslation(translation, request.documentId())
                    .orElseThrow();
            return TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translationId(translationId)
                    .translation(translation.withSourceWords(translation.sourceWords().stream()
                            .filter(word -> !word.isBlank())
                            .distinct()
                            .limit(3)
                            .toList()))
                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(familiarityService.getFamiliarityMap())
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .build();
        } catch (ValidationException e) {
            TranslationAttribute translationAttribute = TranslationAttribute.builder()
                    .selectedWordId(request.selectedWordId())
                    .translation(Translation.builder()
                            .sourceWords(List.of())
                            .targetWord(request.targetWord())
                            .familiarity(request.familiarity())
                            .sourceLanguage(request.sourceLanguage())
                            .targetLanguage(request.targetLanguage())
                            .owner(principal.username())
                            .isPhrase(request.isPhrase())
                            .unprocessedTargetWord("")
                            .build())
                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(request.familiarity()))
                    .familiarityLevels(familiarityService.getFamiliarityMap())
                    .translationId(-1)
                    .documentId(request.documentId())
                    .isPhrase(request.isPhrase())
                    .build();
            throw new TranslationServiceException(translationAttribute, e.getViolations());
        }
    }

    @Transactional
    public TranslationAttribute findByTargetWord(@NonNull FindTranslationFormRequest request,
                                                 @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        String sanitizedTargetWord = patternService.removeSpecialCharacters(request.targetWord())
                .trim()
                .toLowerCase();
        validator.validate(List.of(
                Constraint.define("targetWord", sanitizedTargetWord::isBlank, "blank"),
                Constraint.define("targetWord", () -> sanitizedTargetWord.length() > 150, "too long")
        ));
        return translationRepository.findByTargetWord(sanitizedTargetWord, principal.username())
                .map(translation -> TranslationAttribute.builder()
                        .selectedWordId(request.selectedWordId())
                        .translation(translation.withSourceWords(translation.sourceWords().stream()
                                .filter(word -> !word.isBlank())
                                .distinct()
                                .limit(3)
                                .toList()))
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                        .familiarityLevels(familiarityService.getFamiliarityMap())
                        .translationId(-1)
                        .documentId(request.documentId())
                        .isPhrase(request.isPhrase())
                        .type(TranslationFormType.FIND)
                        .build())
                .orElseGet(() ->
                        Stream.of(
                                translationRepository.findMostFrequentSourceWords(request.targetWord(), 3),
                                translationFetchingService.fetchTranslationsAsync(principal.sourceLanguage(), principal.targetLanguage(), request.targetWord())
                        )
                        .map(fetchedSourceWords -> fetchedSourceWords.stream()
                                .filter(word -> !word.isBlank())
                                .distinct()
                                .limit(3)
                                .toList())
                        .map(fetchedSourceWords -> Translation.builder()
                                .sourceWords(fetchedSourceWords)
                                .targetWord(request.targetWord())
                                .familiarity(Familiarity.UNKNOWN)
                                .sourceLanguage(principal.sourceLanguage())
                                .targetLanguage(principal.targetLanguage())
                                .owner(principal.username())
                                .isPhrase(request.isPhrase())
                                .unprocessedTargetWord("")
                                .build())
                        .map(translation -> TranslationAttribute.builder()
                                .selectedWordId(request.selectedWordId())
                                .translation(translation)
                                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                                .familiarityLevels(familiarityService.getFamiliarityMap())
                                .translationId(-1)
                                .documentId(request.documentId())
                                .isPhrase(request.isPhrase())
                                .type(TranslationFormType.FIND)
                                .build())
                        .findFirst()
                        .get());
    }

    @Transactional
    public TranslationAttribute updateFamiliarity(@NonNull UpdateTranslationFamiliarityRequest request,
                                                  @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Translation translation = translationRepository.updateFamiliarity(request.familiarity(), request.targetWord(), principal.username())
                .orElseThrow(() -> new RuntimeException("Failed to update familiarity for " + request.targetWord()));
        return TranslationAttribute.builder()
                .selectedWordId(request.selectedWordId())
                .translation(translation.withSourceWords(translation.sourceWords().stream()
                        .filter(word -> !word.isBlank())
                        .distinct()
                        .limit(3)
                        .toList()))
                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                .familiarityLevels(familiarityService.getFamiliarityMap())
                .translationId(-1)
                .documentId(-1)
                .isPhrase(request.isPhrase())
                .build();
    }

    @Transactional
    public TranslationAttribute updateSourceWords(@NonNull UpdateSourceWordsRequest request,
                                                  @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        try {
            String sanitizedSourceWord = request.sourceWords().stream()
                    .findFirst()
                    .map(String::trim)
                    .map(patternService::removeSpecialCharacters)
                    .orElse("");
            validator.validate(List.of(
                    Constraint.define("sourceWord", () -> sanitizedSourceWord.length() > 150, switch(uiLanguage) {
                        case PL -> "Słowo źródłowe nie może być dłuższe niż 150 znaków";
                        case EN -> "Source word cannot be longer than 150 characters";
                        case IT -> "La parola sorgente non può superare i 150 caratteri";
                        case NO -> "Kildeordet kan ikke være lengre enn 150 tegn";
                    }),
                    Constraint.define("sourceWord", sanitizedSourceWord::isBlank, switch(uiLanguage) {
                        case PL -> "Słowo źródłowe nie może być puste";
                        case EN -> "Source word cannot be empty";
                        case IT -> "La parola sorgente non può essere vuota";
                        case NO -> "Kildeordet kan ikke være tomt";
                    })
            ));
            return translationRepository.updateSourceWords(request.sourceWords(), request.targetWord(), principal.username())
                    .map(translation -> TranslationAttribute.builder()
                        .documentId(-1)
                        .selectedWordId(request.selectedWordId())
                        .translationId(-1)
                        .translation(translation.withSourceWords(translation.sourceWords().stream()
                                .filter(word -> !word.isBlank())
                                .distinct()
                                .limit(3)
                                .toList()))
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                        .familiarityLevels(familiarityService.getFamiliarityMap())
                        .isPhrase(request.isPhrase())
                    .build())
                    .orElseThrow();
        } catch (ValidationException e) {
            Translation translation = Translation.builder()
                    .sourceWords(request.sourceWords().stream()
                            .map(patternService::removeSpecialCharacters)
                            .filter(word -> !word.isBlank())
                            .toList())
                    .targetWord(request.targetWord())
                    .familiarity(request.familiarity())
                    .sourceLanguage(principal.sourceLanguage())
                    .targetLanguage(principal.targetLanguage())
                    .owner(principal.username())
                    .isPhrase(request.isPhrase())
                    .unprocessedTargetWord("")
                    .build();
            TranslationAttribute translationAttribute = TranslationAttribute.builder()
                    .documentId(-1)
                    .selectedWordId(request.selectedWordId())
                    .translationId(-1)
                    .translation(translation.withSourceWords(translation.sourceWords().stream()
                            .filter(word -> !word.isBlank())
                            .distinct()
                            .limit(  3)
                            .toList()))
                    .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                    .familiarityLevels(familiarityService.getFamiliarityMap())
                    .isPhrase(request.isPhrase())
                    .build();
            throw new TranslationServiceException(translationAttribute, e.getViolations());
        }
    }

    @Transactional
    public TranslationAttribute deleteSourceWord(DeleteSourceWordRequest request, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        String sanitizedSourceWord = patternService.removeSpecialCharacters(request.sourceWord()).trim();
        validator.validate(List.of(
                Constraint.define("sourceWord", () -> sanitizedSourceWord.length() > 150, switch(uiLanguage) {
                    case PL -> "Słowo źródłowe nie może być dłuższe niż 150 znaków";
                    case EN -> "Source word cannot be longer than 150 characters";
                    case IT -> "La parola sorgente non può superare i 150 caratteri";
                    case NO -> "Kildeordet kan ikke være lengre enn 150 tegn";
                }),
                Constraint.define("sourceWord", sanitizedSourceWord::isBlank, switch(uiLanguage) {
                    case PL -> "Słowo źródłowe nie może być puste";
                    case EN -> "Source word cannot be empty";
                    case IT -> "La parola sorgente non può essere vuota";
                    case NO -> "Kildeordet kan ikke være tomt";
                })
        ));
        return translationRepository.deleteSourceWord(request.sourceWord(), request.targetWord(), principal.username())
                .map(translation -> TranslationAttribute.builder()
                        .documentId(-1)
                        .selectedWordId(request.selectedWordId())
                        .translationId(-1)
                        .translation(translation.withSourceWords(translation.sourceWords().stream()
                                .filter(word -> !word.isBlank())
                                .distinct()
                                .limit(3)
                                .toList()))
                        .currentFamiliarity(familiarityService.getFamiliarityAsDigit(translation.familiarity()))
                        .familiarityLevels(familiarityService.getFamiliarityMap())
                        .isPhrase(request.isPhrase())
                        .build())
                .orElseThrow();
    }

    public int getWordsLearnedCount(UserProfile principal) {
        return translationRepository.getWordsLearnedCount(principal.getUsername(), principal.targetLanguage());
    }

    public Map<String, Translation> findTranslationsInDocument(FindTranslationsInDocumentRequest request) {
        List<String> wordList = Arrays.stream(patternService.replaceNewlinesWithSpaces(request.contentBlob()).split(" "))
                .map(patternService::removeSpecialCharacters)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(word -> !word.isEmpty())
                .distinct()
                .toList();
        return translationRepository.findByTargetWords(wordList, request.owner()).stream()
                .distinct()
                .collect(Collectors.toUnmodifiableMap(Translation::targetWord, value -> value));
    }

    public List<Translation> extractPhrases(ExtractPhrasesRequest request) {
        return translationRepository.extractPhrases(request.content(), request.owner());
    }

    public TranslationAttribute translate(CreateTranslationFormRequest request, @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(patternService.removeSpecialCharacters(request.targetWord()).toLowerCase(), 3);
        List<String> sourceWordsFromService = translationFetchingService.fetchTranslationsAsync(principal.sourceLanguage(), principal.targetLanguage(), request.targetWord());
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
                        .sourceLanguage(principal.sourceLanguage())
                        .targetLanguage(principal.targetLanguage())
                        .owner(principal.username())
                        .isPhrase(request.isPhrase())
                        .unprocessedTargetWord("")
                        .build())
                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(Familiarity.UNKNOWN))
                .isPhrase(request.isPhrase())
                .familiarityLevels(familiarityService.getFamiliarityMap())
                .type(TranslationFormType.CREATE)
                .build();
    }

    public BaseFlashcardAttribute getRandomTranslations(GetRandomTranslationsRequest request, Authentication authentication) throws TranslationsNotFoundException {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        List<Translation> translations = translationRepository.getRandomTranslations(request.isPhrase(), principal.getUsername(), request.quantity(), request.familiarity());
        if(!translations.isEmpty()) {
            return BaseFlashcardAttribute.builder()
                    .id(request.id())
                    .size(translations.size())
                    .familiarity(request.familiarity())
                    .quantity(request.quantity())
                    .isPhrase(request.isPhrase())
                    .translations(translations)
                    .localization(translationLocalization.translationFormMessages(principal.userInterfaceLanguage()))
                    .build();
        }
        throw new TranslationsNotFoundException("No translations found to review", FlashcardConfigAttribute.builder()
                .familiarity(request.familiarity())
                .quantity(request.quantity())
                .isPhrase(request.isPhrase())
                .localization(translationLocalization.translationFormMessages(principal.userInterfaceLanguage()))
                .build());

    }

    public FlashcardConfigAttribute getFlashcardConfig(ConfigureFlashcardRequest request, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        return FlashcardConfigAttribute.builder()
                .familiarity(request.familiarity())
                .quantity(request.quantity())
                .isPhrase(request.isPhrase())
                .localization(translationLocalization.translationFormMessages(uiLanguage))
                .build();
    }

    public BaseFlashcardAttribute flipFlashcard(@NonNull FlipFlashcardRequest request, @NonNull Authentication authentication, @NonNull HttpSession session) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        List<Translation> translations = sessionHelper.getList(session, "translations", Translation.class);
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

    public TranslationAttribute findOrCreateTranslation(GetTranslationFormRequest request, Authentication authentication) {
        return switch (request) {
            case CreateTranslationFormRequest createTranslationRequest -> translate(createTranslationRequest, authentication);
            case FindTranslationFormRequest findTranslationRequest -> findByTargetWord(findTranslationRequest, authentication);
        };
    }

    public Map<TranslationLocalizationKey, String> getTranslationFormMessages(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return translationLocalization.translationFormMessages(principal.userInterfaceLanguage());
    }

    public PhraseAttribute createPhraseAttribute(CreatePhraseAttributeRequest request, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        List<String> sourceWordsFromDatabase = translationRepository.findMostFrequentSourceWords(patternService.removeSpecialCharacters(String.join(" ", request.unprocessedTargetWords().stream()
                .map(patternService::removeSpecialCharacters)
                .toList()))
                .toLowerCase(), 3);
        List<String> sourceWordsFromService = translationFetchingService.fetchTranslationsAsync(principal.sourceLanguage(), principal.targetLanguage(), String.join(" ", request.unprocessedTargetWords()));
        List<String> sourceWords = Stream.concat(sourceWordsFromDatabase.stream(), sourceWordsFromService.stream())
                .filter(word -> !word.isBlank())
                .distinct()
                .limit(3)
                .toList();
        List<Translation> phraseParts = IntStream.range(0, request.ids().size())
                .mapToObj(id -> Translation.builder()
                        .sourceWords(List.of())
                        .targetWord(patternService.removeSpecialCharacters(request.unprocessedTargetWords().get(id)))
                        .unprocessedTargetWord(request.unprocessedTargetWords().get(id))
                        .familiarity(switch (request.familiarities().get(id).toUpperCase()) {
                            case "UNKNOWN" -> Familiarity.UNKNOWN;
                            case "RECOGNIZED" -> Familiarity.RECOGNIZED;
                            case "FAMILIAR" -> Familiarity.FAMILIAR;
                            case "KNOWN" -> Familiarity.KNOWN;
                            default -> Familiarity.IGNORED;
                        })
                        .sourceLanguage(principal.sourceLanguage())
                        .targetLanguage(principal.targetLanguage())
                        .owner(principal.username())
                        .isPhrase(false)
                        .build())
                .toList();
        Translation phrase = Translation.builder()
                .sourceWords(sourceWords)
                .targetWord(String.join(" ", request.unprocessedTargetWords().stream()
                        .map(patternService::removeSpecialCharacters)
                        .toList()))
                .unprocessedTargetWord(String.join(" ", request.unprocessedTargetWords()))
                .familiarity(Familiarity.UNKNOWN)
                .sourceLanguage(principal.sourceLanguage())
                .targetLanguage(principal.targetLanguage())
                .owner(principal.username())
                .isPhrase(true)
                .build();
        TranslationAttribute attribute = TranslationAttribute.builder()
                .documentId(request.documentId())
                .selectedWordId(request.id())
                .translationId(-1)
                .translation(phrase)
                .currentFamiliarity(familiarityService.getFamiliarityAsDigit(Familiarity.UNKNOWN))
                .isPhrase(true)
                .familiarityLevels(familiarityService.getFamiliarityMap())
                .type(TranslationFormType.CREATE)
                .build();
        return PhraseAttribute.builder()
                .phrasePartsAttribute(PhrasePartsAttribute.of(phraseParts, request.ids(), request.isPersistedList().stream()
                        .map(isPersisted -> switch (isPersisted){
                            case "true" -> Boolean.TRUE;
                            case "false" -> Boolean.FALSE;
                            default -> throw new RuntimeException("not implemented");
                        })
                        .toList()))
                .phraseAttribute(attribute)
                .id(request.id())
                .documentId(request.documentId())
                .build();
    }
}
